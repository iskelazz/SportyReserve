package es.udc.psi.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import es.udc.psi.model.Reserve;
import es.udc.psi.model.User;
import es.udc.psi.repository.impl.BookRepositoryImpl;
import es.udc.psi.repository.impl.UserRepositoryImpl;
import es.udc.psi.repository.interfaces.BookRepository;
import es.udc.psi.repository.interfaces.UserRepository;
import es.udc.psi.view.interfaces.LoginView;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView signUpTextView;
    private LoginController loginController;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializa las variables de los elementos de la interfaz de usuario
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signUpTextView = findViewById(R.id.signUpTextView);

        // Inicializa el controlador de inicio de sesión
        userRepository = new UserRepositoryImpl();
        loginController = new LoginControllerImpl(this,userRepository);
        // Configura los listeners para los botones
        setupListeners();
        //createRandomReserve();
    }

    private void setupListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required.");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required.");
            return;
        }

        loginController.login(email, password);
    }

    private void openRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginSuccess() {
        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onLoginFailed(String errorMessage) {
        Toast.makeText(LoginActivity.this, "Login Failed: " + errorMessage, Toast.LENGTH_SHORT).show();
    }

    public void createRandomReserve() {
        // Crear usuarios
        User user1 = new User();
        user1.setId("AeZRgPueqHRjR1avUjDTvsO30qm2");
        user1.setNombre("cantona");
        user1.setApellidos("mastuerzo");
        user1.setCorreoElectronico("cantona@mail.com");
        user1.setContraseña("1234567890");
        user1.setTelefono("614614777");

        User user2 = new User();
        user2.setId("Nv56BVdKd6SRCxTuakn1KNHDwC82");
        user2.setNombre("Melero");
        user2.setApellidos("Melonero");
        user2.setCorreoElectronico("melon@mailmelon.com");
        user2.setContraseña("1234567890");
        user2.setTelefono("600112233");

        ArrayList<User> playerList = new ArrayList<>();
        playerList.add(user1);
        playerList.add(user2);

        String id = "RandomId3"; // Este debe ser un identificador único. Puedes usar UUID.randomUUID().toString() para generar uno.
        String anfitrion = "Nv56BVdKd6SRCxTuakn1KNHDwC82";
        String pista = "Pista de tenis de Betanzos";
        int capacidadMax = 2;
        String deporte = "Tennis";
        int numPlayers = 2;

        // Establecer fecha
        Calendar calendar = new GregorianCalendar(2023, Calendar.JUNE, 16, 18, 0);
        Date fecha = calendar.getTime();

        int duracion = 2;

        Reserve reserve = new Reserve(id, anfitrion, pista, capacidadMax, deporte, numPlayers, fecha, duracion, playerList);
        BookRepositoryImpl bookRepository = new BookRepositoryImpl();
        bookRepository.createReserve(reserve, new BookRepository.OnBookCreatedListener() {
            @Override
            public void onSuccess() {
                System.out.println("Reserva creada con éxito");
            }

            @Override
            public void onFailure(String errorMessage) {
                System.err.println("Error al crear la reserva: " + errorMessage);
            }
        });
    }
}