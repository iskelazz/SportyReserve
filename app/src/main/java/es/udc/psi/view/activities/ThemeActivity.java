package es.udc.psi.view.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import es.udc.psi.R;
import es.udc.psi.databinding.ActivityThemeBinding;
import es.udc.psi.utils.CommonThings;

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

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_YES:
                binding.rbDarkTheme.setChecked(true);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
            default:
                binding.rbLightTheme.setChecked(true);
                break;
        }

        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_light_theme:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    saveUserModeNight(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
                case R.id.rb_dark_theme:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    saveUserModeNight(AppCompatDelegate.MODE_NIGHT_YES);
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

    private void saveUserModeNight(int modeNight){
        SharedPreferences.Editor editor = getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE).edit();
        editor.putInt(CommonThings.USER_THEME, modeNight);
        editor.apply();
    }

}
