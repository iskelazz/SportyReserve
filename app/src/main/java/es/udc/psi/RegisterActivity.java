package es.udc.psi;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText, phoneEditText, firstNameEditText, lastNameEditText, passwordEditText, usernameEditText;
    private TextInputLayout emailTextInputLayout, phoneTextInputLayout, firstNameTextInputLayout, lastNameTextInputLayout, passwordTextInputLayout, usernameTextInputLayout;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

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

        // Inicializa Firebase Auth y DatabaseReference
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        // Configura el listener para el botón de registro
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isEmailValid = validateEmail();
                boolean isPasswordValid = validatePassword();
                boolean isPhoneValid = validatePhone();
                boolean isFirstNameValid = validateFirstName();
                boolean isLastNameValid = validateLastName();
                boolean isUsernameValid = validateUsername();

                if (!isEmailValid || !isPasswordValid || !isPhoneValid || !isFirstNameValid || !isLastNameValid || !isUsernameValid) {
                    return;
                }
                    registerUser();
            }
        });
    }

    private boolean validateEmail() {
        String emailInput = emailEditText.getText().toString().trim();
        int errorColor = ContextCompat.getColor(getApplicationContext(), R.color.error_color);
        int defaultColor = ContextCompat.getColor(getApplicationContext(), R.color.default_box_stroke_color);

        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            emailTextInputLayout.setError("Por favor, introduce un correo electrónico válido");
            emailTextInputLayout.setBoxStrokeColor(errorColor);
            return false;
        } else {
            emailTextInputLayout.setError(null);
            emailTextInputLayout.setBoxStrokeColor(defaultColor);
            return true;
        }
    }

    private boolean validatePhone() {
        String phoneInput = phoneEditText.getText().toString().trim();
        int errorColor = ContextCompat.getColor(getApplicationContext(), R.color.error_color);
        int defaultColor = ContextCompat.getColor(getApplicationContext(), R.color.default_box_stroke_color);

       if (!Patterns.PHONE.matcher(phoneInput).matches()) {
            phoneTextInputLayout.setError("Por favor, introduce un número de teléfono válido");
            phoneTextInputLayout.setBoxStrokeColor(errorColor);
            return false;
        } else {
            phoneTextInputLayout.setError(null);
            phoneTextInputLayout.setBoxStrokeColor(defaultColor);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = passwordEditText.getText().toString().trim();
        int errorColor = ContextCompat.getColor(getApplicationContext(), R.color.error_color);
        int defaultColor = ContextCompat.getColor(getApplicationContext(), R.color.default_box_stroke_color);

        if (passwordInput.length() < 8) {
            passwordTextInputLayout.setError("La contraseña debe tener al menos 8 caracteres");
            passwordTextInputLayout.setBoxStrokeColor(errorColor);
            return false;
        } else {
            passwordTextInputLayout.setError(null);
            passwordTextInputLayout.setBoxStrokeColor(defaultColor);
            return true;
        }
    }

    private boolean validateFirstName() {
        String firstNameInput = firstNameEditText.getText().toString().trim();
        int errorColor = ContextCompat.getColor(getApplicationContext(), R.color.error_color);
        int defaultColor = ContextCompat.getColor(getApplicationContext(), R.color.default_box_stroke_color);

        if (firstNameInput.isEmpty()) {
            firstNameTextInputLayout.setError("El campo no puede estar vacío");
            firstNameTextInputLayout.setBoxStrokeColor(errorColor);
            return false;
        } else {
            firstNameTextInputLayout.setError(null);
            firstNameTextInputLayout.setBoxStrokeColor(defaultColor);
            return true;
        }
    }

    private boolean validateLastName() {
        String lastNameInput = lastNameEditText.getText().toString().trim();
        int errorColor = ContextCompat.getColor(getApplicationContext(), R.color.error_color);
        int defaultColor = ContextCompat.getColor(getApplicationContext(), R.color.default_box_stroke_color);

        if (lastNameInput.isEmpty()) {
            lastNameTextInputLayout.setError("El campo no puede estar vacío");
            lastNameTextInputLayout.setBoxStrokeColor(errorColor);
            return false;
        } else {
            lastNameTextInputLayout.setError(null);
            lastNameTextInputLayout.setBoxStrokeColor(defaultColor);
            return true;
        }
    }

    private boolean validateUsername() {
        String usernameInput = usernameEditText.getText().toString().trim();
        int errorColor = ContextCompat.getColor(getApplicationContext(), R.color.error_color);
        int defaultColor = ContextCompat.getColor(getApplicationContext(), R.color.default_box_stroke_color);

        if (usernameInput.length() < 3) {
            usernameTextInputLayout.setError("El nombre de usuario debe tener al menos 3 caracteres");
            usernameTextInputLayout.setBoxStrokeColor(errorColor);
            return false;
        } else {
            usernameTextInputLayout.setError(null);
            usernameTextInputLayout.setBoxStrokeColor(defaultColor);
            return true;
        }
    }
    private void registerUser() {
        final String email = emailEditText.getText().toString().trim();
        final String phone = phoneEditText.getText().toString().trim();
        final String firstName = firstNameEditText.getText().toString().trim();
        final String lastName = lastNameEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();
        final String username = usernameEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)
                || TextUtils.isEmpty(password) || TextUtils.isEmpty(username)) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        mDatabase.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(RegisterActivity.this, "Username is already taken.", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String userId = mAuth.getCurrentUser().getUid();
                                DatabaseReference userRef = mDatabase.child(userId);

                                HashMap<String, Object> userData = new HashMap<>();
                                userData.put("email", email);
                                userData.put("phone", phone);
                                userData.put("firstName", firstName);
                                userData.put("lastName", lastName);
                                userData.put("username", username);
                                userRef.setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "Failed to store user data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(RegisterActivity.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(RegisterActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

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
