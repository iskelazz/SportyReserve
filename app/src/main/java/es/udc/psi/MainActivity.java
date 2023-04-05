package es.udc.psi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private TextView welcomeTextView;
    private Button logoutButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeTextView = findViewById(R.id.welcomeTextView);
        logoutButton = findViewById(R.id.logoutButton);

        mAuth = FirebaseAuth.getInstance();

        setupListeners();
    }
    private void setupListeners() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            welcomeTextView.setText("Welcome, " + currentUser.getEmail());
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}