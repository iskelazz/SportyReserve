package es.udc.psi.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import es.udc.psi.R;
import es.udc.psi.view.adapters.SectionsPagerAdapter;

import androidx.viewpager2.widget.ViewPager2;
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        selectNavigationMenuItem(R.id.nav_mis_reservas);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        FloatingActionButton fab = findViewById(R.id.fab_add_reservation);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BookActivity.class);
                startActivity(intent);
            }
        });

        View headerView = navigationView.getHeaderView(0);
        TextView usernameText = headerView.findViewById(R.id.username_text);
        TextView emailText = headerView.findViewById(R.id.email_text);

        usernameText.setText("Nombre de usuario");
        emailText.setText("Correo electrónico");

        setupTabLayoutAndViewPager(viewPager, tabLayout);
        setupBottomNavigationView();
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
            case R.id.nav_mis_reservas:
                // Lanza la actividad de mis reservas
                // Reemplaza MisReservasActivity.class con la clase de actividad adecuada
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_lista_reservas:
                // Lanza la actividad de lista de reservas
                intent = new Intent(this, ReservesListActivity.class);
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
                //intent = new Intent(this, RegisterActivity.class);
                //startActivity(intent);
                about();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupTabLayoutAndViewPager(ViewPager2 viewPager, TabLayout tabs) {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabs, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Anfitrión");
                    break;
                case 1:
                    tab.setText("Mis Reservas");
                    break;
            }
        });
        tabLayoutMediator.attach();
    }

    private void setupBottomNavigationView() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
// Inicio seleccionado, no se necesita hacer nada aquí
                return true;
            } else if (itemId == R.id.navigation_notifications) {
// Lanza la actividad de notificaciones
// Reemplaza NotificacionesActivity.class con la clase de actividad adecuada
                Intent intent = new Intent(MainActivity.this, NotificationsActivity.class);
                startActivity(intent);
                return true;
            } else {
                return false;
            }
        });
    }

    private void selectNavigationMenuItem(int menuItemId) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        MenuItem menuItem = navigationView.getMenu().findItem(menuItemId);
        if (menuItem != null) {
            menuItem.setChecked(true);
        }
    }


    private void about() {
        //TODO hay que añadirle margenes al layout de "about"
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        builder.setView(getLayoutInflater().inflate(R.layout.activity_about, null));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    
}
