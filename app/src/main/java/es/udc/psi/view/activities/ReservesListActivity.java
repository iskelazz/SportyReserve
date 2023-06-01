package es.udc.psi.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import es.udc.psi.R;
import es.udc.psi.controller.impl.BookControllerImpl;
import es.udc.psi.controller.impl.ReservesListControllerImpl;
import es.udc.psi.controller.interfaces.BookController;
import es.udc.psi.controller.interfaces.ReservesListController;
import es.udc.psi.databinding.ActivityReservesListBinding;
import es.udc.psi.model.Reserve;
import es.udc.psi.model.Sport;
import es.udc.psi.model.Track;
import es.udc.psi.repository.impl.SportRepositoryImpl;
import es.udc.psi.repository.impl.TrackRepositoryImpl;
import es.udc.psi.repository.interfaces.BookRepository;
import es.udc.psi.repository.interfaces.SportRepository;
import es.udc.psi.repository.interfaces.TrackRepository;
import es.udc.psi.view.activities.adapter.ReservesAdapter2;
import es.udc.psi.view.interfaces.ReservesListView;

public class ReservesListActivity extends AppCompatActivity implements ReservesListView {

    public static String ALL_COURTS_AND_SPORTS = "    ";
    private ActivityReservesListBinding binding;
    private ReservesListController mReserveListController;
    private BookController mBookController;
    private ReservesAdapter2 mReserveAdapter;

    private final Calendar startDate = new GregorianCalendar();
    private final Calendar endDate = new GregorianCalendar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityReservesListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        setUpRV();
        setUI();

        mReserveListController = new ReservesListControllerImpl(this);
        mReserveListController.initFlow();
        mBookController = new BookControllerImpl();

    }

    @Override
    public void showReservesList(ArrayList<Reserve> reservesList) {

        mReserveAdapter.setReserveList(reservesList);
        if (reservesList!=null && !reservesList.isEmpty()){
            binding.tvReservesEmptyList.setVisibility(View.GONE);
            binding.rvReserves.setVisibility(View.VISIBLE);
        } else {
            showEmptyView();
        }
    }

    @Override
    public void showError(String error) {

        Toast.makeText(this, getResources().getText(R.string.str_error_msg).toString()+error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyView() {

        binding.tvReservesEmptyList.setVisibility(View.VISIBLE);
        binding.rvReserves.setVisibility(View.GONE);

    }

    @Override
    public void updateReserveListView(Reserve reserve,
                              int position) {
        mReserveAdapter.updateReserve(reserve, position);

    }

    @Override
    public void showAddUser(@NonNull Reserve reserve, int position, boolean comeFromWrongPasswd) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View inputPasswdView = inflater.inflate(R.layout.alertdialog_inputpasswd, null);
        TextInputLayout layoutInputPasswd = inputPasswdView.findViewById(R.id.ad_layoutInputPasswd);
        TextInputEditText inputPasswd = inputPasswdView.findViewById(R.id.ad_et_password);

        if (!reserve.isPublic()){

            builder.setView(inputPasswdView);

            if (comeFromWrongPasswd) {
                //builder.setIcon(getColoredIcon("#FF0000"));
                layoutInputPasswd.setError(getString(R.string.str_alert_wrongpasswd));
                layoutInputPasswd.setBoxStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.error_color));
            }
        }

        builder
                .setTitle(R.string.str_alert_adduser_title)
                .setMessage(getString(R.string.str_alert_adduser_message)+reserve.getName())
                .setPositiveButton(R.string.str_alert_continue, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        String textPasswd = inputPasswd.getText().toString();
                        if (reserve.isPublic()){
                            mReserveListController.addMeAsPlayer(reserve, position);
                        } else if(textPasswd.equals(reserve.getPassword())) {
                            mReserveListController.addMeAsPlayer(reserve, position);
                        } else {

                            dialog.dismiss();
                            showAddUser(reserve, position, true);
                        }

                    }
                } )
                .setNegativeButton(R.string.str_alert_cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();;
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public void showDeleteUser(@NonNull Reserve reserve, int position, boolean comeFromWrongPasswd) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
/*
//TODO: Pedir passwd para salir de reserva privada ??????

        LayoutInflater inflater = this.getLayoutInflater();
        View inputPasswdView = inflater.inflate(R.layout.alertdialog_inputpasswd, null);
        TextInputLayout layoutInputPasswd = inputPasswdView.findViewById(R.id.ad_layoutInputPasswd);
        EditText inputPasswd = inputPasswdView.findViewById(R.id.ad_et_password);

        if (!reserve.isPublic()) {
            builder.setView(inputPasswdView);
            if (comeFromWrongPasswd) {
                layoutInputPasswd.setError("Mal password");
                layoutInputPasswd.setBoxStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.error_color));
            }
        }*/

        builder.setTitle(R.string.str_alert_deleteuser_title)
                .setMessage(getString(R.string.str_alert_deleteuser_message)+reserve.getName())
                .setPositiveButton(R.string.str_alert_continue, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {

                        mReserveListController.deleteMeAsPlayer(reserve, position);

                        //TODO: Pedir passwd para salir de reserva privada ??????
                        /*
                        String textPasswd = inputPasswd.getText().toString();
                        if (reserve.isPublic()){
                            mReserveListController.deleteMeAsPlayer(reserve, position);
                        } else if(textPasswd.equals(reserve.getPassword())) {
                            mReserveListController.deleteMeAsPlayer(reserve, position);
                        } else {
                            showDeleteUser(reserve,position, true);
                        }*/
                    }
                } )

                .setNegativeButton(R.string.str_alert_cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();;
                    }
                });

        builder.setIcon(getColoredIcon("#FFD700"));  //TODO: Crear un icono por cada deporte???

        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public void showImHost() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.str_alert_imhost_title)
                .setMessage(R.string.str_alert_imhost_message)
                .setPositiveButton(R.string.str_alert_continue, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();

                    }
                });

        builder.setIcon(getColoredIcon("#FF0000"));
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void showImInReserve() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.str_alert_iminreserve_title)
                .setPositiveButton(R.string.str_alert_continue, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();

                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private Drawable getColoredIcon(String hexColor) {
        Drawable warningIcon = ContextCompat.getDrawable(ReservesListActivity.this, android.R.drawable.ic_dialog_alert);
        if (warningIcon != null) {
            warningIcon.mutate();
            int color = Color.parseColor(hexColor);
            warningIcon.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        }
        return warningIcon;
    }

    private void setUI() {

        startDate.set(Calendar.HOUR_OF_DAY,0);
        startDate.set(Calendar.MINUTE,0);
        startDate.set(Calendar.SECOND,0);

        endDate.set(Calendar.HOUR_OF_DAY,23);
        endDate.set(Calendar.MINUTE,59);
        endDate.set(Calendar.SECOND,59);

        SportRepository sportRepository = new SportRepositoryImpl();
        TrackRepository tracksRepository = new TrackRepositoryImpl();

        ArrayAdapter<String> sportsAdapter = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                new ArrayList<String>());
        binding.BookSportsDropdownList.setAdapter(sportsAdapter);

        ArrayAdapter<String> tracksAdapter = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                new ArrayList<String>());
        binding.BookCourtsDropdownList.setAdapter(tracksAdapter);

        sportRepository.retrieveSports(new SportRepository.OnSportsRetrievedListener() {

            @Override
            public void onFetched(ArrayList<Sport> result) {
                sportsAdapter.add(ALL_COURTS_AND_SPORTS);
                for (Sport sport : result) {
                    sportsAdapter.add(sport.getName());
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });

        tracksRepository.retrieveTracks(new TrackRepository.OnTracksRetrievedListener() {

            @Override
            public void onFetched(ArrayList<Track> result) {
                tracksAdapter.add(ALL_COURTS_AND_SPORTS);
                for (Track track : result) {
                    tracksAdapter.add(track.getName());
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });

        binding.startDatePickerInput.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                showStartDatePickerDialog();
            }
        });

        binding.endDatePickerInput.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                showEndDatePickerDialog();
            }
        });



        binding.btnFiltrarReservas.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (endDate.getTimeInMillis() >= startDate.getTimeInMillis()){

                    binding.startDatePickerInput.setText(dateToString(startDate.getTime()));
                    binding.endDatePickerInput.setText(dateToString(endDate.getTime()));
                    mBookController.fetchFilteredReserves(
                        binding.BookCourtsDropdownList.getSelectedItem().toString(),
                        binding.BookSportsDropdownList.getSelectedItem().toString(),
                        startDate,
                        endDate,
                        new BookRepository.OnFilteredReservesFetchedListener() {

                            @Override
                            public void onFetched(ArrayList<Reserve> reservesList) {

                                if (reservesList!=null && !reservesList.isEmpty()){

                                    showReservesList(reservesList);

                                } else {

                                    showEmptyView();
                                }
                            }

                            @Override
                            public void onFailure(String error) {
                                showError(error);
                            }
                        });
            } else {
                    showError(getResources().getString(R.string.str_error_searchdates));
                    showEmptyView();
                }
            }
        });

    }


    public String dateToString(Date date){
        DateFormat dfi = new java.text.SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        return dfi.format(date);
    }

    private void showStartDatePickerDialog() {
        DatePickerDialog datePicker = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        startDate.set(Calendar.YEAR, datePicker.getYear());
                        startDate.set(Calendar.MONTH, datePicker.getMonth());
                        startDate.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                        binding.startDatePickerInput.setText(dateToString(startDate.getTime()));
                    }
                }, startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));
        datePicker.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        datePicker.show();
    }
    private void showEndDatePickerDialog() {
        DatePickerDialog datePicker = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        endDate.set(Calendar.YEAR, datePicker.getYear());
                        endDate.set(Calendar.MONTH, datePicker.getMonth());
                        endDate.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                        binding.endDatePickerInput.setText(dateToString(endDate.getTime()));
                    }
                }, endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH));
        datePicker.getDatePicker().setMinDate(startDate.getTimeInMillis());
        datePicker.show();
    }


    private void setUpRV() {

        binding.rvReserves.setHasFixedSize(true);
        mReserveAdapter = new ReservesAdapter2();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rvReserves.setLayoutManager(linearLayoutManager);
        binding.rvReserves.setAdapter(mReserveAdapter);

        mReserveAdapter.setClickListener(new ReservesAdapter2.OnItemClickListener() {

            @Override
            public void onClick(View view,
                                int position) {

                mReserveListController.onClickPlayer(mReserveAdapter.getReserve(position), view.getId());

            }
        });

    }

}