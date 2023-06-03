package es.udc.psi.view.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.bumptech.glide.Glide;

import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.UUID;

import es.udc.psi.R;
import es.udc.psi.controller.impl.UserControllerImpl;
import es.udc.psi.controller.interfaces.UserController;
import es.udc.psi.model.User;
import es.udc.psi.repository.interfaces.UserRepository;

import es.udc.psi.utils.CommonThings;
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
    private ImageView avatarImage;
    private ActivityMainBinding binding;

    private static final int RC_GET_AVATAR_IMAGE = 1234567;
  
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
                stopPersistingEmailPassword();
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
        avatarImage = headerView.findViewById(R.id.avatar_image);
        avatarImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getAvatarImageFromDevice();
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
                usernameText.setText(user.getUsername());
                emailText.setText(user.getCorreoElectronico());
                Glide.with(MainActivity.this)
                        .load(user.getUriAvatar())
                        .placeholder(R.drawable.baseline_account_circle_24)
                        .skipMemoryCache(true)
                        .signature(new ObjectKey(UUID.randomUUID().toString()))
                        .into(avatarImage);
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

    private void getAvatarImageFromDevice(){
        Intent intentAvatarImage = new Intent(Intent.ACTION_GET_CONTENT);
        //Intent intentAvatarImage = new Intent(Intent.ACTION_PICK);
        intentAvatarImage.setType("image/*");

        if (intentAvatarImage.resolveActivity(getPackageManager()) != null) {

            selectAvatarResultLauncher.launch(Intent.createChooser(intentAvatarImage, getString(R.string.str_title_chooser_avatarImage)));
        }

    }

    ActivityResultLauncher<Intent> selectAvatarResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == AppCompatActivity.RESULT_OK) {

                    Intent data = result.getData();
                    if (data != null) {
                            final Uri imageUri = data.getData();
                            Glide.with(MainActivity.this)
                                    .load(imageUri)
                                    .placeholder(R.drawable.baseline_account_circle_24)
                                    .skipMemoryCache(true)
                                    .signature(new ObjectKey(UUID.randomUUID().toString()))
                                    .into(avatarImage);
                            //avatarImage.setImageURI(imageUri);
                            userController.uploadAvatarAndSetUrlAvatar(imageUri);
                    }
                }
            });



    private void stopPersistingEmailPassword(){
        //SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        SharedPreferences.Editor editor = getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE).edit();
        editor.putString(CommonThings.USER_EMAIL, null);
        editor.putString(CommonThings.USER_PASSWORD, null);
        editor.apply();
    }
}
