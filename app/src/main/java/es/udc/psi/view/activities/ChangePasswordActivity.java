package es.udc.psi.view.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import es.udc.psi.R;
import es.udc.psi.model.User;
import es.udc.psi.repository.impl.UserRepositoryImpl;
import es.udc.psi.repository.interfaces.UserRepository;

public class ChangePasswordActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputEditText oldPasswordEditText, newPasswordEditText;
    private Button acceptButton;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_activity);

        toolbar = findViewById(R.id.change_password_toolbar);
        oldPasswordEditText = findViewById(R.id.oldPasswordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        acceptButton = findViewById(R.id.acceptButton);
        userRepository = new UserRepositoryImpl();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Asegúrate de que SettingsActivity está en la pila de back stack
                finish();
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = oldPasswordEditText.getText().toString();
                String newPassword = newPasswordEditText.getText().toString();

                userRepository.getUser(userRepository.getCurrentUserId(), new UserRepository.OnUserFetchedListener() {
                    @Override
                    public void onFetched(User user) {
                        userRepository.updateUserPassword(user, oldPassword, newPassword, new UserRepository.OnPasswordChangedListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(ChangePasswordActivity.this, "Contraseña cambiada correctamente", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailure(String error) {
                                Toast.makeText(ChangePasswordActivity.this, "Error al cambiar contraseña: " + error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(ChangePasswordActivity.this, "Error al obtener información del usuario: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
