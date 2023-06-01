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
import androidx.core.view.GravityCompat;

import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;


import es.udc.psi.R;
import es.udc.psi.controller.impl.UserControllerImpl;
import es.udc.psi.controller.interfaces.UserController;
import es.udc.psi.model.User;
import es.udc.psi.repository.interfaces.UserRepository;

import es.udc.psi.view.fragments.NotificationsFragment;
import es.udc.psi.view.fragments.TabFragment;
import es.udc.psi.databinding.ActivityMainBinding;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private UserController userController;
    private TextView usernameText;
    private TextView emailText;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setupToolbar();
        setupDrawerLayout();
        setupNavigationView();
        setupUserController();
        setupBottomNavigationView();
        setupFragmentTabs();
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateNavigationDrawerSelection();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
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
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_lista_reservas:
                intent = new Intent(this, ReservesListActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_opciones:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_about:
                about();
                break;
            case R.id.nav_logout:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * Configura la barra de herramientas (Toolbar) de la actividad.
     */
    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
    }

    /**
     * Configura el Navigation Drawer (Menú lateral) y el Toggle asociado con la barra de herramientas.
     */
    private void setupDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * Configura la vista de navegación lateral (Navigation View) y selecciona el primer elemento.
     */
    private void setupNavigationView() {
        binding.navView.setNavigationItemSelectedListener(this);
        selectNavigationMenuItem(R.id.nav_mis_reservas);

        View headerView = binding.navView.getHeaderView(0);
        usernameText = headerView.findViewById(R.id.username_text);
        emailText = headerView.findViewById(R.id.email_text);
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
    private void setupFragmentTabs() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        TabFragment tabFragment = new TabFragment();
        fragmentTransaction.add(R.id.fragment_tabs, tabFragment); // Aquí utilizamos la misma ID
        fragmentTransaction.commit();
    }

    /**
     * Configura la vista de navegación inferior (Bottom Navigation View).
     */
    private void setupBottomNavigationView() {
        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        selectedFragment = new TabFragment();
                        break;
                    case R.id.navigation_notifications:
                        selectedFragment = new NotificationsFragment();
                        break;
                }
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_tabs, selectedFragment).commit();
                    return true;
                }
                return false;
            }
        });
    }

    private void selectNavigationMenuItem(int menuItemId) {
        MenuItem menuItem = binding.navView.getMenu().findItem(menuItemId);
        if (menuItem != null) {
            menuItem.setChecked(true);
        }
    }


    private void about() {
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
                // Handle the error
            }
        });
    }

    private void updateNavigationDrawerSelection() {
        MenuItem menuItem;
        // Update selection based on the current activity
        if (this instanceof MainActivity) {
            menuItem = binding.navView.getMenu().findItem(R.id.nav_mis_reservas);
        }else {
            return;
        }
        if (menuItem != null) {
            menuItem.setChecked(true);
        }
    }
}
