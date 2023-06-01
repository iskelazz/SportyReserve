package es.udc.psi.view.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import es.udc.psi.R;
import es.udc.psi.controller.impl.LoginControllerImpl;
import es.udc.psi.controller.interfaces.LoginController;
import es.udc.psi.databinding.ActivityLoginBinding;
import es.udc.psi.model.Reserve;
import es.udc.psi.model.User;
import es.udc.psi.repository.impl.BookRepositoryImpl;
import es.udc.psi.repository.impl.UserRepositoryImpl;
import es.udc.psi.repository.interfaces.BookRepository;
import es.udc.psi.repository.interfaces.UserRepository;
import es.udc.psi.utils.CommonThings;
import es.udc.psi.utils.ReservationReminderManager;
import es.udc.psi.utils.ResourceDemocratizator;
import es.udc.psi.view.interfaces.LoginView;

public class LoginActivity extends AppCompatActivity implements LoginView {


    private ActivityLoginBinding binding;
    private LoginController loginController;
    private UserRepository userRepository;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Inicializa la clase singleton que permite a las clases que no son activities, acceder a
        // los recursos
        /** TIENE QUE IR EN PRIMER LUGAR EN LA APP **/
        ResourceDemocratizator.initialize(getApplicationContext());

        // Inicializa el controlador de inicio de sesión
        userRepository = new UserRepositoryImpl();
        loginController = new LoginControllerImpl(this,userRepository);

        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(CommonThings.USER_EMAIL, null);
        String password = sharedPreferences.getString(CommonThings.USER_PASSWORD,null);
        if ((email == null) && (password==null)){
            setupListeners();
            //createRandomReserve();

        } else {

            loginController.login(email,password);
        }

    }

    private void setupListeners() {
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        binding.signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });
    }

    private void loginUser() {
        String email = binding.emailEditText.getText().toString().trim();
        String password = binding.passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            binding.emailEditText.setError(getString(R.string.Error_EmailIsRequired));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            binding.passwordEditText.setError(getString(R.string.Error_PasswordIsRequired));
            return;
        }

        loginController.login(email, password);
    }

    private void openRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginSuccess(String email, String password) {
        Toast.makeText(LoginActivity.this, getString(R.string.Toast_LoginSuccesfull), Toast.LENGTH_SHORT).show();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CommonThings.USER_EMAIL, email);
        editor.putString(CommonThings.USER_PASSWORD, password);
        editor.apply();

        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onLoginFailed(String errorMessage) {
        Toast.makeText(LoginActivity.this, getString(R.string.Error_LoginFailed) + errorMessage, Toast.LENGTH_SHORT).show();
    }
}
