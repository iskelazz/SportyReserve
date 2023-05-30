package es.udc.psi.view.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import es.udc.psi.R;
import es.udc.psi.databinding.ActivityThemeBinding;

public class ThemeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityThemeBinding binding = ActivityThemeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.themeToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Aquí se debe recuperar el tema actual y seleccionar el RadioButton correspondiente.
        // Supongamos que el tema se almacena en SharedPreferences.

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.rbDarkTheme.setChecked(true);
        } else {
            binding.rbLightTheme.setChecked(true);
        }

        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
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
