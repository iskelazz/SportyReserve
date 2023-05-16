package es.udc.psi.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;

import es.udc.psi.R;
import es.udc.psi.controller.impl.BookControllerImpl;
import es.udc.psi.controller.impl.RegisterControllerImpl;
import es.udc.psi.view.interfaces.BookView;
import es.udc.psi.view.interfaces.RegisterView;

public class BookActivity extends AppCompatActivity implements BookView {

    private EditText passwordEditText;
    private TextInputLayout passwordTextInputLayout;
    private ToggleButton privatenessTButton;
    private Spinner sportsDropdownSpinner;
    private Spinner tracksDropdownSpinner;
    private Button bookButton;
    private BookControllerImpl bookController;

    // TODO Futuras mejoras (todavía mockup) - Meter ints y utilizar recursos
    private String[] sportList = new String[]{"Fútbol", "Baloncesto", "Tenis", "Padel"};
    private String[] tracksList = new String[]{"Badalona", "Carabamchel", "AtapuercaFC"};

    /**
     * Se activa el campo "password" en función del valor del "toggle" de visibilidad
     */
    private void visibilityCongruence()
    {
        if(privatenessTButton.isChecked())
            passwordEditText.setEnabled(true);
        else
            passwordEditText.setEnabled(false);
    }

    /**
     * @param value : Se pone el botón de visibilidad a "value" y se adecúa la visibilidad del
     *                campo "password" a ello
     */
    private void setVisibilityWithCongruence(boolean value)
    {
        privatenessTButton.setChecked(value);

        passwordEditText.setEnabled(value);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        // Sports Spinner
        sportsDropdownSpinner = findViewById(R.id.Book_SportsDropdownList);
        ArrayAdapter<String> sportsAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, sportList);
        sportsDropdownSpinner.setAdapter(sportsAdapter);

        // Tracks Spinner
        tracksDropdownSpinner = findViewById(R.id.Book_TracksDropdownList);
        ArrayAdapter<String> tracksAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tracksList);
        tracksDropdownSpinner.setAdapter(tracksAdapter);

        // Botón visibilidad
        privatenessTButton = findViewById(R.id.book_privateness_button);
        privatenessTButton.setEnabled(true);


        // Inicializa las variables de los elementos de la interfaz de usuario
        passwordEditText = findViewById(R.id.Book_passwordEditText);
        // Lo activo o desactivo en función del valor de la visibilidad de la reserva
        visibilityCongruence();

        bookButton = findViewById(R.id.bookButton);

        passwordTextInputLayout = findViewById(R.id.Book_passwordTextInputLayout);

        passwordEditText.addTextChangedListener(fieldsTextWatcher);

        // Establecer la opacidad inicial del botón de registro
        updateRegisterButtonOpacity(false);

        // Inicializa el controlador de registro
        bookController = new BookControllerImpl();

        /*
        // Configura el listener para el botón de registro
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerController.validateAndRegister(
                        emailEditText.getText().toString().trim(),
                        phoneEditText.getText().toString().trim(),
                        firstNameEditText.getText().toString().trim(),
                        lastNameEditText.getText().toString().trim(),
                        passwordEditText.getText().toString().trim(),
                        usernameEditText.getText().toString().trim()
                );
            }
        });
        */
    }

    // Métodos de la interfaz RegisterView

    @Override
    public void onBookSuccess() {
        Toast.makeText(this, "Registration successful.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(BookActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onBookFailure(String errorMessage) {
        Toast.makeText(this, "Registration Failed: " + errorMessage, Toast.LENGTH_SHORT).show();
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
            updateRegisterButtonOpacity(allFieldsFilled);
        }
    };

    private boolean areAllFieldsFilled() {
        return true;
                /*
                !TextUtils.isEmpty(sportsDropdownSpinner.get.getText().toString().trim())
                && !TextUtils.isEmpty(phoneEditText.getText().toString().trim())
                && !TextUtils.isEmpty(firstNameEditText.getText().toString().trim())
                && !TextUtils.isEmpty(lastNameEditText.getText().toString().trim())
                && !TextUtils.isEmpty(passwordEditText.getText().toString().trim())
                && !TextUtils.isEmpty(usernameEditText.getText().toString().trim());
                 */
    }

    private void updateRegisterButtonOpacity(boolean enabled) {
        float alpha = enabled ? 1.0f : 0.5f;
        bookButton.setAlpha(alpha);
    }
}