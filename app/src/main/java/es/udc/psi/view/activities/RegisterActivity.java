package es.udc.psi.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import es.udc.psi.R;
import es.udc.psi.controller.impl.RegisterControllerImpl;
import es.udc.psi.databinding.ActivityRegisterBinding;
import es.udc.psi.view.interfaces.RegisterView;

public class RegisterActivity extends AppCompatActivity implements RegisterView {

    private ActivityRegisterBinding binding;
    private RegisterControllerImpl registerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.emailEditText.addTextChangedListener(fieldsTextWatcher);
        binding.phoneEditText.addTextChangedListener(fieldsTextWatcher);
        binding.firstNameEditText.addTextChangedListener(fieldsTextWatcher);
        binding.lastNameEditText.addTextChangedListener(fieldsTextWatcher);
        binding.passwordEditText.addTextChangedListener(fieldsTextWatcher);
        binding.nicknameEditText.addTextChangedListener(fieldsTextWatcher);

        // Establecer la opacidad inicial del botón de registro
        updateRegisterButtonOpacity(false);

        // Inicializa el controlador de registro
        registerController = new RegisterControllerImpl(this);

        // Configura el listener para el botón de registro
        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerController.validateAndRegister(
                        binding.emailEditText.getText().toString().trim(),
                        binding.phoneEditText.getText().toString().trim(),
                        binding.firstNameEditText.getText().toString().trim(),
                        binding.lastNameEditText.getText().toString().trim(),
                        binding.passwordEditText.getText().toString().trim(),
                        binding.nicknameEditText.getText().toString().trim()
                );
            }
        });
    }

    // Métodos de la interfaz RegisterView

    @Override
    public void onRegisterSuccess() {
        Toast.makeText(this, getString(R.string.succesfull_registration), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onRegisterFailure(String errorMessage) {
        Toast.makeText(this, getString(R.string.failure_on_registration) + errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showValidationError(String fieldName, String errorMessage) {
        switch (fieldName) {
            case "email":
                binding.emailTextInputLayout.setError(errorMessage);
                binding.emailTextInputLayout.setBoxStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.error_color));
                break;
            case "phone":
                binding.phoneTextInputLayout.setError(errorMessage);
                binding.phoneTextInputLayout.setBoxStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.error_color));
                break;
            case "firstName":
                binding.firstNameTextInputLayout.setError(errorMessage);
                binding.firstNameTextInputLayout.setBoxStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.error_color));
                break;
            case "lastName":
                binding.lastNameTextInputLayout.setError(errorMessage);
                binding.lastNameTextInputLayout.setBoxStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.error_color));
                break;
            case "password":
                binding.passwordTextInputLayout.setError(errorMessage);
                binding.passwordTextInputLayout.setBoxStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.error_color));
                break;
            case "username":
                binding.nicknameTextInputLayout.setError(errorMessage);
                binding.nicknameTextInputLayout.setBoxStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.error_color));
                break;
        }
    }

    @Override
    public void clearValidationError(String fieldName) {
        switch (fieldName) {
            case "email":
                binding.emailTextInputLayout.setError(null);
                binding.emailTextInputLayout.setBoxStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.default_box_stroke_color));
                break;
            case "phone":
                binding.phoneTextInputLayout.setError(null);
                binding.phoneTextInputLayout.setBoxStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.default_box_stroke_color));
                break;
            case "firstName":
                binding.firstNameTextInputLayout.setError(null);
                binding.firstNameTextInputLayout.setBoxStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.default_box_stroke_color));
                break;
            case "lastName":
                binding.lastNameTextInputLayout.setError(null);
                binding.lastNameTextInputLayout.setBoxStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.default_box_stroke_color));
                break;
            case "password":
                binding.passwordTextInputLayout.setError(null);
                binding.passwordTextInputLayout.setBoxStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.default_box_stroke_color));
                break;
            case "username":
                binding.nicknameTextInputLayout.setError(null);
                binding.nicknameTextInputLayout.setBoxStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.default_box_stroke_color));
                break;
        }
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
            binding.registerButton.setEnabled(allFieldsFilled);
            updateRegisterButtonOpacity(allFieldsFilled);
        }
    };

    private boolean areAllFieldsFilled() {
        return !TextUtils.isEmpty(binding.emailEditText.getText().toString().trim())
                && !TextUtils.isEmpty(binding.phoneEditText.getText().toString().trim())
                && !TextUtils.isEmpty(binding.firstNameEditText.getText().toString().trim())
                && !TextUtils.isEmpty(binding.lastNameEditText.getText().toString().trim())
                && !TextUtils.isEmpty(binding.passwordEditText.getText().toString().trim())
                && !TextUtils.isEmpty(binding.nicknameEditText.getText().toString().trim());
    }

    private void updateRegisterButtonOpacity(boolean enabled) {
        float alpha = enabled ? 1.0f : 0.5f;
        binding.registerButton.setAlpha(alpha);
    }
}
