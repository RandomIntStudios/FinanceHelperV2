package com.ristudios.financehelperv2.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ristudios.financehelperv2.R;
import com.ristudios.financehelperv2.data.Item;
import com.ristudios.financehelperv2.data.ItemManager;
import com.ristudios.financehelperv2.ui.adapter.ItemAdapter;
import com.ristudios.financehelperv2.ui.fragments.DeleteDialog;
import com.ristudios.financehelperv2.utils.Utils;


import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class CalendarDayActivity extends BaseActivity implements ItemManager.ItemManagerListener, ItemAdapter.AdapterClickListener, DeleteDialog.DeleteDialogListener {

    private ZonedDateTime zonedDateTime;
    private ItemManager itemManager;
    private ItemAdapter itemAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_calendar_day);
        super.onCreate(savedInstanceState);
        initBurgerMenu();
        getSupportActionBar().setTitle(getString(R.string.calendar));
        initData();
        initUI();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Initiates the data of the current activity.
     */
    private void initData() {
        itemManager = new ItemManager(this, this);
        itemAdapter = new ItemAdapter(this, this);
        zonedDateTime = ZonedDateTime.now().with(LocalDate.of(getIntent().getIntExtra("year", 0), getIntent().getIntExtra("month", 1), getIntent().getIntExtra("day", 1)));
        RecyclerView recyclerView = findViewById(R.id.recycler_calendar_day);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemManager.loadItemsForDate(zonedDateTime.getYear(), zonedDateTime.getMonthValue(), zonedDateTime.getDayOfMonth());
    }

    /**
     * Initiates the UI of the Activity.
     */
    private void initUI() {
        TextView txtHeader = findViewById(R.id.txt_detail_header);
        txtHeader.setText(Utils.getLocalizedFormattedShortDate(zonedDateTime));
        MaterialButton btnPreviousDate = findViewById(R.id.btn_previoius_date);
        MaterialButton btnNextDate = findViewById(R.id.btn_next_date);
        btnPreviousDate.setOnClickListener(v -> {
            zonedDateTime = zonedDateTime.minusDays(1);
            updateData();
        });
        btnNextDate.setOnClickListener(v -> {
            zonedDateTime = zonedDateTime.plusDays(1);
            updateData();
        });

    }

    private void updateSubTotal() {
        float total = 0;
        for (Item item : itemManager.getItems()) {
            if (item.isIncome()) {
                total = total + item.getPriceTotal();
            } else {
                total = total - item.getPriceTotal();
            }
        }
        if (total != 0) {
            total = Utils.roundToTwoDecimals(total);
        }
        TextView txtSubTotalValue = findViewById(R.id.txt_total_price);
        txtSubTotalValue.setText(getResources().getString(R.string.currency_sign_value).replace("$VALUE", String.valueOf(total)));
    }

    /**
     * Updates the displayed data when the buttons for next or previous day are clicked.
     */
    private void updateData() {
        TextView txtHeader = findViewById(R.id.txt_detail_header);
        txtHeader.setText(Utils.getLocalizedFormattedShortDate(zonedDateTime));
        itemManager.loadItemsForDate(zonedDateTime.getYear(), zonedDateTime.getMonthValue(), zonedDateTime.getDayOfMonth());
        //ArrayList<Entry> sortedList = Utils.sortListByCategory(entryManager.getCurrentEntries());

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == R.id.nav_calendar){

            finish();
            return false;
        }
        else {
            Intent intent = getIntent();
            intent.putExtra("finishParent", true);
            setResult(RESULT_CANCELED, intent);
            super.onNavigationItemSelected(item);
            return false;
        }
    }

    @Override
    public void onItemListUpdated() {
        runOnUiThread(() -> {
            itemAdapter.updateList(itemManager.getItems());
            itemAdapter.notifyDataSetChanged();
            updateSubTotal();
        });
    }

    @Override
    public void onListLoaded() {
        itemManager.updateData();
    }


    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra("resultYear", zonedDateTime.getYear());
        intent.putExtra("resultMonth", zonedDateTime.getMonthValue());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onItemLayoutClicked(Item item, int pos) {

    }

    @Override
    public void onItemLayoutLongClicked(Item item, int pos) {
        Toast.makeText(this, "Deleting items here will be added in later versions", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClicked(Item toDelete) {

    }
}