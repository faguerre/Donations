package com.donation;


import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.donation.database.LoginDBHandler;
import com.donation.database.TokenHandler;
import com.donation.geofencing.DonationGeoReferences;
import com.google.android.material.navigation.NavigationView;

public class MenuActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private TokenHandler tokenHandler;
    private LoginDBHandler loginDBHandler;
    private final String[] token = new String[1];
    private Boolean isExternal = false;
    private DonationGeoReferences donationGeoReferences;


    private boolean isLogged() {
        token[0] = tokenHandler.getUserToken();
        isExternal = loginDBHandler.isExternal();
        return (!token[0].equals("")) || isExternal;
    }

    private void settingMenu() {

        tokenHandler = new TokenHandler(getApplicationContext(), null, null, 1);
        loginDBHandler = new LoginDBHandler(getApplicationContext(), null, null, 1);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu miMenu = navigationView.getMenu();

        if (isLogged()) {
            miMenu.findItem(R.id.nav_login).setVisible(false);
            miMenu.findItem(R.id.nav_editInfo).setVisible(!isExternal);
            miMenu.findItem(R.id.createEventFragment).setVisible(true);
            miMenu.findItem(R.id.tagFragment).setVisible(true);
            miMenu.findItem(R.id.donationFragment).setVisible(true);
            miMenu.findItem(R.id.userDonationsViewFragment).setVisible(true);
            miMenu.findItem(R.id.viewConfirmedDonationsFragment).setVisible(true);
            miMenu.findItem(R.id.nav_logout).setVisible(true);
            miMenu.findItem(R.id.donationUserTagFragment).setVisible(true);
        } else {
            miMenu.findItem(R.id.nav_login).setVisible(true);
            miMenu.findItem(R.id.nav_logout).setVisible(false);
            miMenu.findItem(R.id.nav_editInfo).setVisible(false);
            miMenu.findItem(R.id.createEventFragment).setVisible(false);
            miMenu.findItem(R.id.donationFragment).setVisible(false);
            miMenu.findItem(R.id.tagFragment).setVisible(false);
            miMenu.findItem(R.id.userDonationsViewFragment).setVisible(false);
            miMenu.findItem(R.id.donationUserTagFragment).setVisible(false);
            miMenu.findItem(R.id.viewConfirmedDonationsFragment).setVisible(false);
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        settingMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.initialFragment,
                R.id.nav_login,
                R.id.nav_editInfo,
                R.id.donationsViewFragment,
                R.id.nav_eventViewFragment,
                R.id.nav_locationMapFragment,
                R.id.nav_homeFragment,
                R.id.nav_NewUser,
                R.id.nav_logout,
                R.id.donationFragment,
                R.id.createEventFragment,
                R.id.nav_eventViewFragment,
                R.id.eventconfirmation,
                R.id.donateToEvent,
                R.id.tagFragment,
                R.id.donationUserTagFragment,
                R.id.geofencingMapFragment,
                R.id.userDonationsViewFragment,
                R.id.viewConfirmedDonationsFragment
        ).setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        donationGeoReferences = new DonationGeoReferences(getApplicationContext());
        donationGeoReferences.generateList();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        settingMenu();
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Nullable
    @Override
    public ActionBarDrawerToggle.Delegate getDrawerToggleDelegate() {
        settingMenu();
        return super.getDrawerToggleDelegate();
    }
}