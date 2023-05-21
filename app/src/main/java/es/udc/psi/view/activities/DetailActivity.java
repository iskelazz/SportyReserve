package es.udc.psi.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import es.udc.psi.R;
import es.udc.psi.controller.impl.BookControllerImpl;
import es.udc.psi.model.Reserve;
import es.udc.psi.repository.impl.BookRepositoryImpl;
import es.udc.psi.repository.impl.UserRepositoryImpl;
import es.udc.psi.view.adapters.UserAdapter;

public class DetailActivity extends AppCompatActivity implements UserAdapter.OnUserExpelledListener {
    private MaterialButton btnCancel;
    private MaterialButton btnEdit;
    private TextView textViewReserveId;
    private TextView textViewPista;
    private TextView textViewCapacidadMax;
    private TextView textViewDeporte;
    private TextView textViewNumPlayers;
    private TextView textViewFechaIni;
    private TextView textViewHoraIni;
    private TextView textViewFechaFin;
    private TextView textViewHoraFin;
    private Reserve reserve;
    private BookControllerImpl bookController;
    private BookRepositoryImpl bookRepository;
    private UserRepositoryImpl userRepository;
    private String currentUserId;

    private RecyclerView rvPlayerList;
    private static final String EXTRA_RESERVE = "extra_reserve";
    //private UserAdapter adapter;

    public static Intent newIntent(Context context, Reserve reserve) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_RESERVE, reserve);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        bookRepository = new BookRepositoryImpl();
        userRepository = new UserRepositoryImpl();
        currentUserId = userRepository.getCurrentUserId();
        reserve = getIntent().getParcelableExtra(EXTRA_RESERVE);
        bookController= new BookControllerImpl();
        textViewReserveId = findViewById(R.id.text_view_reserve_id);
        textViewReserveId.setText(reserve.getId());

        textViewPista = findViewById(R.id.text_view_pista);
        textViewPista.setText(reserve.getPista());

        textViewCapacidadMax = findViewById(R.id.text_view_num_max);
        textViewCapacidadMax.setText(String.valueOf(reserve.getCapacidadMax()));

        textViewDeporte = findViewById(R.id.text_view_deporte);
        textViewDeporte.setText(reserve.getDeporte());

        textViewNumPlayers = findViewById(R.id.text_view_num_actual);
        textViewNumPlayers.setText(String.valueOf(reserve.getNumPlayers()));

        textViewHoraIni = findViewById(R.id.tvInicio);
        DateFormat df = new SimpleDateFormat("HH:mm", Locale.getDefault());
        textViewHoraIni.setText(df.format(reserve.getFecha()));

        textViewFechaIni = findViewById(R.id.tvFechaInicio);
        DateFormat dfi = new SimpleDateFormat("MM/dd", Locale.getDefault());
        textViewFechaIni.setText(dfi.format(reserve.getFecha()));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(reserve.getFecha());
        calendar.add(Calendar.MINUTE, reserve.getDuracion());
        Date newDate = calendar.getTime();

        textViewHoraFin = findViewById(R.id.tvFinal);
        textViewHoraFin.setText(df.format(newDate));

        textViewFechaFin = findViewById(R.id.tvFechaFin);
        textViewFechaFin.setText(dfi.format(newDate));

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
                textViewNumPlayers.setText(String.valueOf(reserve.getNumPlayers()));
                SetupUserList(); // Actualizar la lista de usuarios con los datos más recientes
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getApplicationContext(), "Error al recuperar los datos de la reserva: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SetupCancelButton() {
        // Initialize cancel button
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(view -> {
            bookController.deleteReserve(reserve.getId(),new BookRepositoryImpl.OnBookDeletedListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(), "Reserva eliminada", Toast.LENGTH_SHORT).show();
                    // Navega a la pantalla de MainActivity
                    Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(getApplicationContext(), "Error al eliminar la reserva: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void SetupEditButton() {
        // Initialize edit button
        btnEdit = findViewById(R.id.btnEdit);

        // Comprueba si el usuario actual es el anfitrión de la reserva
        if (reserve.getAnfitrion().equals(currentUserId)) {
            // Si el usuario actual es el anfitrión, haz que el botón EDIT sea visible
            btnEdit.setVisibility(View.VISIBLE);

            btnEdit.setOnClickListener(view -> {
                // Handle click event here
                // For example, edit the reservation
            });
        } else {
            // Si el usuario actual no es el anfitrión, haz que el botón EDIT sea invisible
            btnEdit.setVisibility(View.GONE);
        }
    }

    private void SetupUserList(){
        rvPlayerList = findViewById(R.id.rvPlayerList);
        rvPlayerList.setLayoutManager(new LinearLayoutManager(this));
        UserAdapter adapter = new UserAdapter(reserve.getPlayerList(), reserve, this);
        rvPlayerList.setAdapter(adapter);
        adapter.setOnUserExpelledListener(this);
    }

    @Override
    public void onUserExpelled() {
        int numPlayers = Integer.parseInt(textViewNumPlayers.getText().toString());
        numPlayers -= 1;
        textViewNumPlayers.setText(String.valueOf(numPlayers));
    }
}
