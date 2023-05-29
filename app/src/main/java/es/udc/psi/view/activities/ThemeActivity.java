package es.udc.psi.view.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import es.udc.psi.R;

public class ThemeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        Toolbar toolbar = findViewById(R.id.theme_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        RadioButton rbLightTheme = findViewById(R.id.rb_light_theme);
        RadioButton rbDarkTheme = findViewById(R.id.rb_dark_theme);

        // Aquí se debe recuperar el tema actual y seleccionar el RadioButton correspondiente.
        // Supongamos que el tema se almacena en SharedPreferences.

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            rbDarkTheme.setChecked(true);
        } else {
            rbLightTheme.setChecked(true);
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_light_theme:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
                case R.id.rb_dark_theme:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
            }
            recreate(); // You need to recreate the activity for changes to take effect
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();  // Cerrar esta actividad y volver a la anterior (SettingsActivity)
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
