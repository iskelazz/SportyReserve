package es.udc.psi.view.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.udc.psi.R;
import es.udc.psi.controller.impl.BookControllerImpl;
import es.udc.psi.model.Reserve;
import es.udc.psi.model.User;
import es.udc.psi.repository.impl.BookRepositoryImpl;
import es.udc.psi.repository.impl.UserRepositoryImpl;
import es.udc.psi.utils.ReservationReminderManager;
import es.udc.psi.view.adapters.UserAdapter;
import es.udc.psi.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity implements UserAdapter.OnUserExpelledListener {
    private Reserve reserve;
    private BookControllerImpl bookController;
    private BookRepositoryImpl bookRepository;
    private UserRepositoryImpl userRepository;
    private String currentUserId;
    private ActivityDetailBinding binding;

    private static final String EXTRA_RESERVE = "extra_reserve";

    public static Intent newIntent(Context context, Reserve reserve) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_RESERVE, reserve);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bookRepository = new BookRepositoryImpl();
        userRepository = new UserRepositoryImpl();
        currentUserId = userRepository.getCurrentUserId();
        reserve = getIntent().getParcelableExtra(EXTRA_RESERVE);
        bookController= new BookControllerImpl();
        binding.textViewReserveId.setText(reserve.getName());
        binding.textViewPista.setText(reserve.getPista());
        binding.textViewNumMax.setText(String.valueOf(reserve.getCapacidadMax()));
        binding.textViewDeporte.setText(reserve.getDeporte());
        binding.textViewNumActual.setText(String.valueOf(reserve.getNumPlayers()));

        DateFormat df = new SimpleDateFormat("HH:mm", Locale.getDefault());
        binding.tvInicio.setText(df.format(reserve.getFecha()));

        DateFormat dfi = new SimpleDateFormat("MM/dd", Locale.getDefault());
        binding.tvFechaInicio.setText(dfi.format(reserve.getFecha()));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(reserve.getFecha());
        calendar.add(Calendar.MINUTE, reserve.getDuracion());
        Date newDate = calendar.getTime();

        binding.tvFinal.setText(df.format(newDate));
        binding.tvFechaFin.setText(dfi.format(newDate));

        SetupUserList();
        SetupCancelButton();
        SetupEditButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bookRepository.getReserve(reserve.getId(), new BookRepositoryImpl.OnReserveRetrievedListener() {
            @Override
            public void onSuccess(Reserve updatedReserve) {
                reserve = updatedReserve;
                binding.textViewNumActual.setText(String.valueOf(reserve.getNumPlayers()));
                SetupUserList();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getApplicationContext(), getString(R.string.reserve_retrieving_failure) + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SetupCancelButton() {
        binding.btnCancel.setOnClickListener(view -> {
            if (reserve.getAnfitrion().equals(currentUserId)) {
                cancelReservationAsHost(reserve.getId());
            } else {
                cancelReservationAsPlayer(reserve.getId(), currentUserId);
            }
        });
    }

    private void SetupEditButton() {
        if (reserve.getAnfitrion().equals(currentUserId)) {
            binding.btnEdit.setVisibility(View.VISIBLE);

            binding.btnEdit.setOnClickListener(view -> {
                // Handle click event here
                // For example, edit the reservation
            });
        } else {
            binding.btnEdit.setVisibility(View.GONE);
        }
    }

    private void SetupUserList(){
        binding.rvPlayerList.setLayoutManager(new LinearLayoutManager(this));
        UserAdapter adapter = new UserAdapter(reserve.getPlayerList(), reserve, this);
        binding.rvPlayerList.setAdapter(adapter);
        adapter.setOnUserExpelledListener(this);
    }

    @Override
    public void onUserExpelled() {
        int numPlayers = Integer.parseInt(binding.textViewNumActual.getText().toString());
        numPlayers -= 1;
        binding.textViewNumActual.setText(String.valueOf(numPlayers));
    }

    private void cancelReservationAsHost(String reserveId) {
        new AlertDialog.Builder(DetailActivity.this)
                .setTitle(getString(R.string.Title_ReserveDeletion))
                .setMessage(getString(R.string.onReserveDeletion_ConfirmationMessage))
                .setPositiveButton(getString(R.string.continueButtonText), (dialog, which) -> {
                    bookController.deleteReserve(reserveId, new BookRepositoryImpl.OnBookDeletedListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getApplicationContext(), getString(R.string.toast_DeletedReserve), Toast.LENGTH_SHORT).show();
                            ReservationReminderManager.cancelReservationReminder(getApplicationContext(), reserveId.hashCode());
                            Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(getApplicationContext(), getString(R.string.toast_FailureOnReserveDeletion) + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    });
                })
                .setNegativeButton(getString(R.string.cancelationButtonText), null)
                .setIcon(getColoredIcon(getString(R.color.cancelationButtonColor)))
                .show();
    }

    private void cancelReservationAsPlayer(String reserveId, String playerId) {
        bookRepository.getReserve(reserveId, new BookRepositoryImpl.OnReserveRetrievedListener() {
            @Override
            public void onSuccess(Reserve reserve) {
                List<User> userList = reserve.getPlayerList();
                userList.removeIf(user -> user.getId().equals(playerId));
                bookRepository.replaceUserListWithNew(reserveId, userList, new BookRepositoryImpl.OnUserListUpdatedListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_YouHaveBeenSuccesfullyRemovedFromReserve), Toast.LENGTH_SHORT).show();
                        ReservationReminderManager.cancelReservationReminder(getApplicationContext(), reserveId.hashCode());
                        Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_FailureOnReserveParticipationRemoval) + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getApplicationContext(), getString(R.string.toast_FailureOnReserveGathering) + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private Drawable getColoredIcon(String hexColor) {
        Drawable warningIcon = ContextCompat.getDrawable(DetailActivity.this, android.R.drawable.ic_dialog_alert);
        if (warningIcon != null) {
            warningIcon.mutate();
            int color = Color.parseColor(hexColor);
            warningIcon.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        }
        return warningIcon;
    }
}
