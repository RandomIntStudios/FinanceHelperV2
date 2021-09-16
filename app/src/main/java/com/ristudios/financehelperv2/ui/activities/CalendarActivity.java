package com.ristudios.financehelperv2.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ristudios.financehelperv2.R;

public class CalendarActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_calendar);
        super.onCreate(savedInstanceState);
        initBurgerMenu();
        getSupportActionBar().setTitle(R.string.calendar);
    }
}