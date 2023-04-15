package es.udc.psi.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;

import es.udc.psi.R;
import es.udc.psi.controller.impl.RegisterControllerImpl;
import es.udc.psi.view.interfaces.RegisterView;

public class RegisterActivity extends AppCompatActivity implements RegisterView {

    private EditText emailEditText, phoneEditText, firstNameEditText, lastNameEditText, passwordEditText, usernameEditText;
    private TextInputLayout emailTextInputLayout, phoneTextInputLayout, firstNameTextInputLayout, lastNameTextInputLayout, passwordTextInputLayout, usernameTextInputLayout;
    private Button registerButton;

    private RegisterControllerImpl registerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializa las variables de los elementos de la interfaz de usuario
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        usernameEditText = findViewById(R.id.nicknameEditText);
        registerButton = findViewById(R.id.registerButton);

        emailTextInputLayout = findViewById(R.id.emailTextInputLayout);
        phoneTextInputLayout = findViewById(R.id.phoneTextInputLayout);
        firstNameTextInputLayout = findViewById(R.id.firstNameTextInputLayout);
        lastNameTextInputLayout = findViewById(R.id.lastNameTextInputLayout);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
        usernameTextInputLayout = findViewById(R.id.nicknameTextInputLayout);

        emailEditText.addTextChangedListener(fieldsTextWatcher);
        phoneEditText.addTextChangedListener(fieldsTextWatcher);
        firstNameEditText.addTextChangedListener(fieldsTextWatcher);
        lastNameEditText.addTextChangedListener(fieldsTextWatcher);
        passwordEditText.addTextChangedListener(fieldsTextWatcher);
        usernameEditText.addTextChangedListener(fieldsTextWatcher);

        // Establecer la opacidad inicial del botón de registro
        updateRegisterButtonOpacity(false);

        // Inicializa el controlador de registro
        registerController = new RegisterControllerImpl(this);

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
    }

    // Métodos de la interfaz RegisterView

    @Override
    public void onRegisterSuccess() {
        Toast.makeText(this, "Registration successful.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onRegisterFailure(String errorMessage) {
        Toast.makeText(this, "Registration Failed: " + errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showValidationError(String fieldName, String errorMessage) {
        TextInputLayout inputLayout;
        switch (fieldName) {
            case "email":
                inputLayout = emailTextInputLayout;
                break;
            case "phone":
                inputLayout = phoneTextInputLayout;
                break;
            case "firstName":
                inputLayout = firstNameTextInputLayout;
                break;
            case "lastName":
                inputLayout = lastNameTextInputLayout;
                break;
            case "password":
                inputLayout = passwordTextInputLayout;
                break;
            case "username":
                inputLayout = usernameTextInputLayout;
                break;
            default:
                return;
        }
        inputLayout.setError(errorMessage);
        inputLayout.setBoxStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.error_color));
    }

    @Override
    public void clearValidationError(String fieldName) {
        TextInputLayout inputLayout;
        switch (fieldName) {
            case "email":
                inputLayout = emailTextInputLayout;
                break;
            case "phone":
                inputLayout = phoneTextInputLayout;
                break;
            case "firstName":
                inputLayout = firstNameTextInputLayout;
                break;
            case "lastName":
                inputLayout = lastNameTextInputLayout;
                break;
            case "password":
                inputLayout = passwordTextInputLayout;
                break;
            case "username":
                inputLayout = usernameTextInputLayout;
                break;
            default:
                return;
        }
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
            registerButton.setEnabled(allFieldsFilled);
            updateRegisterButtonOpacity(allFieldsFilled);
        }
    };

    private boolean areAllFieldsFilled() {
        return !TextUtils.isEmpty(emailEditText.getText().toString().trim())
                && !TextUtils.isEmpty(phoneEditText.getText().toString().trim())
                && !TextUtils.isEmpty(firstNameEditText.getText().toString().trim())
                && !TextUtils.isEmpty(lastNameEditText.getText().toString().trim())
                && !TextUtils.isEmpty(passwordEditText.getText().toString().trim())
                && !TextUtils.isEmpty(usernameEditText.getText().toString().trim());
    }

    private void updateRegisterButtonOpacity(boolean enabled) {
        float alpha = enabled ? 1.0f : 0.5f;
        registerButton.setAlpha(alpha);
    }
}