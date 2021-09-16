package com.ristudios.financehelperv2.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.google.android.material.navigation.NavigationView;
import com.ristudios.financehelperv2.R;

public class BaseActivity extends AppCompatActivity implements DrawerLayout.DrawerListener, NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawerLayout;
    protected ActionBarDrawerToggle drawerToggle;
    protected Toolbar toolbar;
    protected NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBurgerMenu();

    }

    protected void initBurgerMenu() {
        initToolbar();
        initDrawer();
        initToggle();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
    }

    private void initDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.addDrawerListener(this);
    }

    private void initToggle(){
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        invalidateOptionsMenu();
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        invalidateOptionsMenu();
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String classname = this.getClass().getSimpleName();
        switch (item.getItemId()) {
            case R.id.nav_budget: drawerLayout.closeDrawer(GravityCompat.START, true);
                    launchActivity(classname, BudgetActivity.class, true);
                break;
            case R.id.nav_calendar: drawerLayout.closeDrawer(GravityCompat.START, true);
                launchActivity(classname, CalendarActivity.class, false);
                break;
            case R.id.nav_settings: drawerLayout.closeDrawer(GravityCompat.START, true);
                launchActivity(classname, SettingsActivity.class, false);
                break;
            case R.id.nav_info: drawerLayout.closeDrawer(GravityCompat.START, true);
                launchActivity(classname, InfoActivity.class, false);
                break;
            default: return false;
        }
        return false;
    }

    private void launchActivity(String classname, Class activity, boolean backToMain) {
        if (!classname.equals(activity.getSimpleName())) {
            if (!backToMain) {
                Intent intent = new Intent(this, activity);
                startActivity(intent);
            }
            else {
                finish();
            }
        }
        if (!classname.equals("BudgetActivity") && !classname.equals(activity.getSimpleName()))
        {
            finish();
        }
    }

}