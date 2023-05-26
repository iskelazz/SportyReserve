package es.udc.psi.view.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import es.udc.psi.R;
import es.udc.psi.controller.impl.BookControllerImpl;
import es.udc.psi.controller.impl.UserControllerImpl;
import es.udc.psi.controller.interfaces.BookController;
import es.udc.psi.controller.interfaces.UserController;
import es.udc.psi.model.Reserve;
import es.udc.psi.model.User;
import es.udc.psi.repository.interfaces.BookRepository;
import es.udc.psi.repository.interfaces.UserRepository;
import es.udc.psi.view.adapters.SectionsPagerAdapter;
import es.udc.psi.view.fragments.HostFragment;
import es.udc.psi.view.fragments.NotificationsFragment;

import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;
    private UserController userController;
    private TextView usernameText;
    private TextView emailText;
    private SectionsPagerAdapter pagerAdapter;
    private Toolbar toolbar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setupDrawerLayout();
        setupNavigationView();
        setupFloatingActionButton();
        setupUserController();
        setupViewPager();
        setupTabLayoutAndViewPager();
        setupBottomNavigationView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateNavigationDrawerSelection();
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
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_about:
                // Lanza la actividad de "acerca de"
                // Reemplaza AcercaDeActivity.class con la clase de actividad adecuada
                about();
                break;
            case R.id.nav_logout:
                //intent = new Intent(this, RegisterActivity.class);
                //startActivity(intent);
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Configura el TabLayout y lo asocia con el ViewPager.
     */
    private void setupTabLayoutAndViewPager() {
        tabLayout = findViewById(R.id.tabs);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager, (tab, position) -> {
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

    /**
     * Configura la barra de herramientas (Toolbar) de la actividad.
     */
    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * Configura el Navigation Drawer (Menú lateral) y el Toggle asociado con la barra de herramientas.
     */
    private void setupDrawerLayout() {
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * Configura la vista de navegación lateral (Navigation View) y selecciona el primer elemento.
     */
    private void setupNavigationView() {
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        selectNavigationMenuItem(R.id.nav_mis_reservas);

        View headerView = navigationView.getHeaderView(0);
        usernameText = headerView.findViewById(R.id.username_text);
        emailText = headerView.findViewById(R.id.email_text);
    }

    /**
     * Configura el botón flotante de acción (Floating Action Button) asociado a crearReserva.
     */
    private void setupFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab_add_reservation);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BookActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Configura el controlador de usuario (UserController).
     */
    private void setupUserController() {
        userController = new UserControllerImpl();
        setupUser();
    }

    /**
     * Configura el ViewPager para la navegación entre pestañas.
     */
    private void setupViewPager() {
        viewPager = findViewById(R.id.view_pager);
        pagerAdapter = new SectionsPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
    }

    /**
     * Configura la vista de navegación inferior (Bottom Navigation View).
     */
    private void setupBottomNavigationView() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        // Si ya estás en MainActivity, no se necesita hacer nada aquí
                        if (!(MainActivity.this instanceof MainActivity)) {
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        return true;
                    case R.id.navigation_notifications:
                        // Lanza el fragmento de notificaciones
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, new NotificationsFragment());
                        transaction.addToBackStack(null);
                        transaction.commit();
                        return true;
                }
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

    private void setupUser() {
        String uid = userController.getCurrentUserId();
        userController.getUser(uid, new UserRepository.OnUserFetchedListener() {
            @Override
            public void onFetched(User user) {
                usernameText.setText(user.getNombre());
                emailText.setText(user.getCorreoElectronico());
            }

            @Override
            public void onFailure(String error) {
                // Maneja el error
            }
        });
    }

    private void updateNavigationDrawerSelection() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        MenuItem menuItem;
        // Actualiza la selección en base a la actividad actual
        if (this instanceof MainActivity) {
            menuItem = navigationView.getMenu().findItem(R.id.nav_mis_reservas);
        }else {
            return;
        }
        if (menuItem != null) {
            menuItem.setChecked(true);
        }
    }
}

