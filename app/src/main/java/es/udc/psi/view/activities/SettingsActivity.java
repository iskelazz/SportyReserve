package es.udc.psi.view.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import es.udc.psi.R;
import es.udc.psi.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySettingsBinding binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.settingsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.settingsToolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        binding.optionTheme.setOnClickListener(v -> {
            Intent themeActivityIntent = new Intent(SettingsActivity.this, ThemeActivity.class);
            startActivity(themeActivityIntent);
        });

        binding.optionPassword.setOnClickListener(v -> {
            Intent passwordActivityIntent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
            startActivity(passwordActivityIntent);
        });
    }
}
