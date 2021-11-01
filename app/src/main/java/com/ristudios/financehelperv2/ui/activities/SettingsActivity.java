package com.ristudios.financehelperv2.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ristudios.financehelperv2.R;
import com.ristudios.financehelperv2.ui.fragments.SettingsFragment;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_settings);
        super.onCreate(savedInstanceState);
        initBurgerMenu();
        getSupportActionBar().setTitle(R.string.settings);
        getSupportFragmentManager().beginTransaction().add(R.id.container_settings_fragment, new SettingsFragment()).commit();
    }
}