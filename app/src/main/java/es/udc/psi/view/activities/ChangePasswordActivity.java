package es.udc.psi.view.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import es.udc.psi.model.User;
import es.udc.psi.repository.impl.UserRepositoryImpl;
import es.udc.psi.repository.interfaces.UserRepository;
import es.udc.psi.databinding.ChangePasswordActivityBinding;

public class ChangePasswordActivity extends AppCompatActivity {

    private ChangePasswordActivityBinding binding;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ChangePasswordActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRepository = new UserRepositoryImpl();

        setSupportActionBar(binding.changePasswordToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.changePasswordToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Asegúrate de que SettingsActivity está en la pila de back stack
                finish();
            }
        });

        binding.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = binding.oldPasswordEditText.getText().toString();
                String newPassword = binding.newPasswordEditText.getText().toString();

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
