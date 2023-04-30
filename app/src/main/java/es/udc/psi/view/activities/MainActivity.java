package es.udc.psi.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

import es.udc.psi.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent;

        switch (id) {
            case R.id.nav_reservar_pista:
                // Lanza la actividad de reserva de pista
                intent = new Intent(this, BookActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_mis_reservas:
                // Lanza la actividad de mis reservas
                // Reemplaza MisReservasActivity.class con la clase de actividad adecuada
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_lista_reservas:
                // Lanza la actividad de lista de reservas
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_opciones:
                // Lanza la actividad de opciones
                // Reemplaza OpcionesActivity.class con la clase de actividad adecuada
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_about:
                // Lanza la actividad de "acerca de"
                // Reemplaza AcercaDeActivity.class con la clase de actividad adecuada
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
