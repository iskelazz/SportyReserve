package es.udc.psi.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import es.udc.psi.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        MaterialCardView themeOptionCard = findViewById(R.id.option_theme);
        themeOptionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent themeActivityIntent = new Intent(SettingsActivity.this, ThemeActivity.class);
                //startActivity(themeActivityIntent);
            }
        });

        MaterialCardView passwordOptionCard = findViewById(R.id.option_password);
        passwordOptionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent passwordActivityIntent = new Intent(SettingsActivity.this, PasswordActivity.class);
                //startActivity(passwordActivityIntent);
            }
        });
    }
}
