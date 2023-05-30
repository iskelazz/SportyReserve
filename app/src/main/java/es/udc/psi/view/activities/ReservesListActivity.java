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
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

    public static String ALL_COURTS_AND_SPORTS = "__________";
    private ActivityReservesListBinding binding;
    private ReservesListController mReserveListController;
    private BookController mBookController;
    private ReservesAdapter2 mReserveAdapter;

    private Calendar startDate = new GregorianCalendar();
    private Calendar endDate = new GregorianCalendar();

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
    public void showAddUser(@NonNull Reserve reserve, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        TextInputLayout layoutInputPasswd = new TextInputLayout(this);
        final TextInputEditText inputPasswd = new TextInputEditText(this);
        TextInputLayout.LayoutParams lp = new TextInputLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutInputPasswd.setLayoutParams(lp);
        layoutInputPasswd.setHint(getString(R.string.password_register));
        layoutInputPasswd.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
        layoutInputPasswd.setEndIconDrawable(R.drawable.outline_lock_black_20);
        layoutInputPasswd.addView(inputPasswd);

        //inputPasswd.setLayoutParams(lp);
        inputPasswd.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);

        if (!reserve.isPublic()){builder.setView(layoutInputPasswd);}

        builder
                .setTitle("Añadir como jugador")
                .setMessage("Vas a añadirte como jugador a la reserva :    "+reserve.getName())
                .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        String textPasswd = inputPasswd.getText().toString();
                        if (reserve.isPublic()){
                            mReserveListController.addMeAsPlayer(reserve, position);
                        } else if(textPasswd.equals(reserve.getPassword())) {
                            mReserveListController.addMeAsPlayer(reserve, position);
                        } else {
                            //TODO:Error de passwd
                            layoutInputPasswd.setError("Mal password");
                            layoutInputPasswd.setBoxStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.error_color));
                            builder.setIcon(getColoredIcon("#FFD700"));
                        }//?TODO:????????????????????

                    }
                } )
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();;
                    }
                });
                //.setIcon(getColoredIcon("#FFD700"));  //TODO: Crear un icono por cada deporte???
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public void showDeleteUser(@NonNull Reserve reserve, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //TODO:Cambiar UI?????????????
        EditText inputPasswd = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        inputPasswd.setLayoutParams(lp);

        if (!reserve.isPublic()){builder.setView(inputPasswd);} //TODO: Necesario??????


        builder.setTitle("Borrar como jugador")
                .setMessage("Vas a eliminarte como jugador de la reserva : "+reserve.getName())
                .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        String textPasswd = inputPasswd.getText().toString();
                        if (reserve.isPublic()){
                            mReserveListController.deleteMeAsPlayer(reserve, position);
                        } else if(textPasswd.equals(reserve.getPassword())) {
                            mReserveListController.deleteMeAsPlayer(reserve, position);
                        } else {
                            //TODO:Error de passwd????? dialog.
                            builder.setIcon(getColoredIcon("#FFD700"));
                            dialog.dismiss();//?TODO:????????????????????
                        }
                    }
                } )

                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

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
        builder.setTitle("   Eres el Anfitrión")
                .setMessage("No puedes borrarte de la reserva desde aquí")
                .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {

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
        builder.setTitle("Ya estás en la reserva")
                .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {

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

        //TODO:????
        //binding.startDatePickerInput.addTextChangedListener(fieldsTextWatcher);
        //binding.endDatePickerInput.addTextChangedListener(fieldsTextWatcher);

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
            }
        });

    }


    public String localeDate(int day, int month, int year)
    {
        String dateOfGame = Integer.toString(day)+"/"+Integer.toString(month)+"/"+Integer.toString(year);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = sdf.parse(dateOfGame);
        } catch (ParseException e) {
            // handle exception here !
        }
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(this.getBaseContext());
        String s = dateFormat.format(date);

        return s;
    }

    private void showStartDatePickerDialog() {
        DatePickerDialog datePicker = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        startDate.set(Calendar.YEAR, datePicker.getYear());
                        startDate.set(Calendar.MONTH, datePicker.getMonth());
                        startDate.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                        startDate.set(Calendar.HOUR_OF_DAY,0);
                        startDate.set(Calendar.MINUTE,0);
                        startDate.set(Calendar.SECOND,0);
                        binding.startDatePickerInput.setText(localeDate(datePicker.getDayOfMonth(), datePicker.getMonth()+1, datePicker.getYear()));
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
                        endDate.set(Calendar.HOUR_OF_DAY,23);
                        endDate.set(Calendar.MINUTE,59);
                        endDate.set(Calendar.SECOND,59);
                        binding.endDatePickerInput.setText(localeDate(datePicker.getDayOfMonth(), datePicker.getMonth()+1, datePicker.getYear()));
                    }
                }, endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH));
        datePicker.getDatePicker().setMinDate(startDate.getTimeInMillis()); //TODO:??? startDate.getTimeInMillis() ó Calendar.getInstance().getTimeInMillis()
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