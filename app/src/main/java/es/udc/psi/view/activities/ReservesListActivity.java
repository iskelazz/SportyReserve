package es.udc.psi.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
    public void showError() {

        Toast.makeText(this, getResources().getText(R.string.str_error_msg).toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyView() {

        binding.tvReservesEmptyList.setVisibility(View.VISIBLE);
        binding.rvReserves.setVisibility(View.GONE);

    }

    @Override
    public void updateReserve(Reserve reserve,
                              int position) {

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
                sportsAdapter.add("-- Todos --");
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
                tracksAdapter.add("-- Todas --");
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


                Calendar dateprueba = new GregorianCalendar();
                Log.d("TAG_LOG","mes Antes:   "+dateprueba.get(Calendar.MONTH)+" en milliseconds: "+dateprueba.getTimeInMillis());
                dateprueba.set(Calendar.MONTH,0);
                Log.d("TAG_LOG","mes Después: "+dateprueba.get(Calendar.MONTH)+" en milliseconds: "+dateprueba.getTimeInMillis());

                //Log.d("TAG_LOG","hora startDate: "+startDate.get(Calendar.HOUR_OF_DAY)+":"+startDate.get(Calendar.MINUTE)+":"+startDate.get(Calendar.SECOND)+"en milliseconds: "+startDate.getTimeInMillis());
                //Log.d("TAG_LOG","hora endDate  : "+endDate.get(Calendar.HOUR_OF_DAY)+":"+endDate.get(Calendar.MINUTE)+":"+endDate.get(Calendar.SECOND)+"en milliseconds: "+endDate.getTimeInMillis());
                Log.d("TAG_LOG","hora startDate: "+startDate.get(Calendar.DAY_OF_MONTH)+"/"+startDate.get(Calendar.MONTH)+"/"+startDate.get(Calendar.YEAR)+"/       "+startDate.get(Calendar.HOUR_OF_DAY)+":"+startDate.get(Calendar.MINUTE)+":"+startDate.get(Calendar.SECOND)+"en milliseconds: "+startDate.getTimeInMillis());
                Log.d("TAG_LOG","hora endDate:   "+endDate.get(Calendar.DAY_OF_MONTH)+"/"+endDate.get(Calendar.MONTH)+"/"+endDate.get(Calendar.YEAR)+"/       "+endDate.get(Calendar.HOUR_OF_DAY)+":"+endDate.get(Calendar.MINUTE)+":"+endDate.get(Calendar.SECOND)+"en milliseconds: "+endDate.getTimeInMillis());


                mBookController.fetchFilteredReserves(
                        binding.BookCourtsDropdownList.getSelectedItem().toString(),
                        binding.BookSportsDropdownList.getSelectedItem().toString(),
                        startDate,
                        endDate,
                        new BookRepository.OnFilteredReservesFetchedListener() {

                            @Override
                            public void onFetched(ArrayList<Reserve> reservesList) {
                                showReservesList(reservesList);
                            }

                            @Override
                            public void onFailure(String error) {

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
                //TODO: check if player exist or add new player

                Log.d("TAG_GOL","position???: "+ view.getId());
                Log.d("TAG_GOL","REserva?? : "+ mReserveAdapter.getReserve(position).getName());

                mReserveListController.onClickPlayer(mReserveAdapter.getReserve(position), view.getId());
                mReserveListController.onClickAddNewPlayer();

            }
        });

    }

}