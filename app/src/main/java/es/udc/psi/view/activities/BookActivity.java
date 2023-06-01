package es.udc.psi.view.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import es.udc.psi.R;
import es.udc.psi.controller.impl.BookControllerImpl;
import es.udc.psi.controller.impl.UserControllerImpl;
import es.udc.psi.databinding.ActivityBookBinding;
import es.udc.psi.model.Reserve;
import es.udc.psi.model.Sport;

import es.udc.psi.model.Track;
import es.udc.psi.model.User;
import es.udc.psi.repository.impl.SportRepositoryImpl;
import es.udc.psi.repository.impl.TrackRepositoryImpl;
import es.udc.psi.repository.interfaces.SportRepository;
import es.udc.psi.repository.interfaces.TrackRepository;
import es.udc.psi.repository.interfaces.UserRepository;
import es.udc.psi.utils.ReservationReminderManager;
import es.udc.psi.view.interfaces.BookView;

public class BookActivity extends AppCompatActivity implements BookView {

    private ActivityBookBinding binding;
    private EditText passwordEditText;
    private TextInputLayout passwordTextInputLayout;
    private ToggleButton privatenessTButton;
    private Spinner sportsDropdownSpinner;
    private Spinner tracksDropdownSpinner;
    private Button bookButton;

    private ArrayList<Sport> sportsList;
    private UserControllerImpl userController;
    private BookControllerImpl bookController;
    private SportRepositoryImpl sportRepository;
    private TrackRepositoryImpl tracksRepository;

    // TODO Futuras mejoras (todavía mockup) - Meter ints y utilizar recursos
    //private ArrayList<String> sportList = new ArrayList<>();
    private Calendar date = new GregorianCalendar();
    private User currentUser;

    /**
     * Se activa el campo "password" en función del valor del "toggle" de visibilidad
     */
    private void visibilityCongruence()
    {
        // True -> isPublic
        if(privatenessTButton.isChecked())
            passwordEditText.setEnabled(false);
        else
            passwordEditText.setEnabled(true);
    }

    /**
     * @param value : Se pone el botón de visibilidad a "value" y se adecúa la visibilidad del
     *                campo "password" a ello. Si es "true", la reserva se desea pública y entonces
     *                la contraseña no tiene sentido, por tanto se desactiva
     */
    private void setVisibilityWithCongruence(boolean value)
    {
        privatenessTButton.setChecked(value);

        passwordEditText.setEnabled(!value);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBookBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        // Inicializa los controladores de registro y los repositorios (capa modelo)
        bookController = new BookControllerImpl(this);
        userController = new UserControllerImpl();
        sportRepository = new SportRepositoryImpl();
        tracksRepository = new TrackRepositoryImpl();

        // Sports Spinner
        sportsDropdownSpinner = binding.BookSportsDropdownList;
        ArrayAdapter<String> sportsAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, new ArrayList<String>());
        sportsDropdownSpinner.setAdapter(sportsAdapter);

        // Tracks Spinner
        tracksDropdownSpinner = findViewById(R.id.Book_TracksDropdownList);
        ArrayAdapter<String> tracksAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, new ArrayList<String>());
        tracksDropdownSpinner.setAdapter(tracksAdapter);

        // Lengths Spinner
        ArrayAdapter<String> lengthsAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, new String[]{"30", "60", "90", "120"});
        binding.reserveLengthDropdownList.setAdapter(lengthsAdapter);

        // Max Participants
        binding.ReserveMaxParticipantsPicker.setMinValue(1);

        // Get sports, tracks and user data from DB
        sportRepository.retrieveSports(new SportRepository.OnSportsRetrievedListener() {
            @Override
            public void onFetched(ArrayList<Sport> result) {
                sportsList = result;
                for (Sport sport: result)
                {
                    Log.d("Sports retrieval on Book Activity - ", "onFetched: "+sport.getName());
                    sportsAdapter.add(sport.getName());
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });

        tracksRepository.retrieveTracks(new TrackRepository.OnTracksRetrievedListener(){
            @Override
            public void onFetched(ArrayList<Track> result) {
                for (Track track: result)
                {
                    Log.d("Tracks retrieval on Book Activity - ", "onFetched: "+track.getName());
                    tracksAdapter.add(track.getName());
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });

        userController.getUser(userController.getCurrentUserId(), new UserRepository.OnUserFetchedListener() {
            @Override
            public void onFetched(User user) {
                currentUser = user;
            }

            @Override
            public void onFailure(String error) {
                // TODO Lanzar excepcion sobre que no se ha recuperado el usuario, como es un poco
                //      raro lo de traer un usuario para reservar... Se
                //      podría poner un error desconocido
            }
        });

        // Botón visibilidad
        privatenessTButton = findViewById(R.id.book_privateness_button);
        privatenessTButton.setEnabled(true);


        // Inicializa las variables de los elementos de la interfaz de usuario
        passwordEditText = findViewById(R.id.Book_passwordEditText);

        // Lo activo o desactivo en función del valor de la visibilidad de la reserva y se desea que
        // la reserva sea pública por defecto, es más cómodo no tener que añadir una contraseña
        setVisibilityWithCongruence(true);

        bookButton = findViewById(R.id.bookButton);

        passwordTextInputLayout = findViewById(R.id.Book_passwordTextInputLayout);

        passwordEditText.addTextChangedListener(fieldsTextWatcher);
        binding.datePickerInput.addTextChangedListener(fieldsTextWatcher);
        binding.timePickerInput.addTextChangedListener(fieldsTextWatcher);

        // Establecer la opacidad inicial del botón de registro
        updateBookButtonOpacity(false);

        /************** Listeners ***************/
        sportsDropdownSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Supongo que el adapter mantiene el orden que tenían los "sports"
                binding.ReserveMaxParticipantsPicker.setMaxValue(sportsList.get(i).getMaxParticipants());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Not interested now
            }
        });

        binding.datePickerInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        binding.timePickerInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        // Configura el listener para el botón de registro
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(binding.reserveTitleField.getText().toString());
                if (currentUser == null)
                {
                    // Unkown Error al usuario
                }
                ArrayList<User> aux = new ArrayList<>();
                aux.add(currentUser);
                bookController.validateAndRegister(
                        new Reserve(
                                FirebaseAuth.getInstance().getUid(),
                                binding.reserveTitleField.getText().toString(),
                                date.getTime(),
                                (String) tracksDropdownSpinner.getSelectedItem(),
                                (String) sportsDropdownSpinner.getSelectedItem(),
                                Integer.parseInt(binding.reserveLengthDropdownList.getSelectedItem().toString()),
                                privatenessTButton.isChecked(),
                                passwordEditText.getText().toString(),
                                binding.ReserveMaxParticipantsPicker.getValue(),
                                aux
                        ));
            }
        });

        privatenessTButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setVisibilityWithCongruence(b);
                System.out.println(privatenessTButton.isChecked());
            }
        });

    }

    private void showTimePickerDialog() {
        TimePickerDialog timePicker = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        date.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                        date.set(Calendar.MINUTE, timePicker.getMinute());
                        binding.timePickerInput.setText(
                                String.format("%02d:%02d",
                                        date.get(Calendar.HOUR_OF_DAY),
                                        date.get(Calendar.MINUTE))
                        );
                    }
                }, date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE), true);
        timePicker.show();
    }

    // TODO Add this function to somewhere so it can be used from anywhere
    public String localeDate(int day, int month, int year)
    {
        String dateOfBirth = Integer.toString(day)+"/"+Integer.toString(month)+"/"+Integer.toString(year);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = sdf.parse(dateOfBirth);
        } catch (ParseException e) {
            // handle exception here !
        }
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(this.getBaseContext());
        String s = dateFormat.format(date);

        return s;
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePicker = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        date.set(Calendar.YEAR, datePicker.getYear());
                        date.set(Calendar.MONTH, datePicker.getMonth());
                        date.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                        binding.datePickerInput.setText(localeDate(datePicker.getDayOfMonth(), datePicker.getMonth()+1, datePicker.getYear()));
                    }
                }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
        datePicker.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        datePicker.show();
    }

    // Métodos de la interfaz RegisterView

    @Override
    public void onBookSuccess(Reserve reserve) {
        Toast.makeText(this, getString(R.string.succesfull_registration), Toast.LENGTH_SHORT).show();
        scheduleNotification(reserve);
        startActivity(new Intent(BookActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onBookFailure(String errorMessage) {
        Toast.makeText(this, getString(R.string.failure_on_registration) + errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showValidationError(String fieldName, String errorMessage) {
        TextInputLayout inputLayout;
        switch (fieldName) {
            case "duracion":
                //inputLayout = emailTextInputLayout;
                break;
            case "password":
                inputLayout = passwordTextInputLayout;
                break;
            default:
                return;
        }
        // Línea temporal mientras no le damos un valor a "InputLayout" desde el caso "duracion"
        inputLayout = passwordTextInputLayout;
        inputLayout.setError(errorMessage);
        inputLayout.setBoxStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.error_color));
    }

    @Override
    public void clearValidationError(String fieldName) {
        TextInputLayout inputLayout;
        switch (fieldName) {
            case "date":
                //inputLayout = emailTextInputLayout;
                break;
            case "password":
                inputLayout = passwordTextInputLayout;
                break;
            default:
                return;
        }
        // Línea temporal mientras no le damos un valor a "InputLayout" desde el caso "duracion"
        inputLayout = passwordTextInputLayout;
        inputLayout.setError(null);
        inputLayout.setBoxStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.default_box_stroke_color));
    }

// TextWatcher y otros métodos auxiliares

    TextWatcher fieldsTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // No es necesario implementar este método
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // No es necesario implementar este método
        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean allFieldsFilled = areAllFieldsFilled();
            bookButton.setEnabled(allFieldsFilled);
            updateBookButtonOpacity(allFieldsFilled);
        }
    };

    private boolean areAllFieldsFilled() {
        return  !TextUtils.isEmpty(binding.datePickerInput.getText().toString().trim())
                && !TextUtils.isEmpty(binding.reserveTitleField.getText().toString().trim())
                && (!TextUtils.isEmpty(passwordEditText.getText().toString().trim()) || privatenessTButton.isChecked()); // If isPublic, then no password is needed;
    }

    private void updateBookButtonOpacity(boolean enabled) {
        float alpha = enabled ? 1.0f : 0.5f;
        bookButton.setAlpha(alpha);
    }

    public void datePicker(DatePicker date) {

    }

    public void scheduleNotification(Reserve reserve) {
        // Obtén la hora de la reserva y réstale una hora
        Calendar oneHourBefore = Calendar.getInstance();
        oneHourBefore.setTime(reserve.getFecha());
        oneHourBefore.add(Calendar.HOUR, -1);
        String idUser = userController.getCurrentUserId();
        // Usa directamente oneHourBefore, que ya es un Calendar
        ReservationReminderManager.scheduleReservationReminder(getApplicationContext(), oneHourBefore, reserve.getId().hashCode(), reserve.getName(),idUser);
    }
}