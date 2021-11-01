package com.ristudios.financehelperv2.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ristudios.financehelperv2.R;
import com.ristudios.financehelperv2.data.Item;
import com.ristudios.financehelperv2.data.ItemManager;
import com.ristudios.financehelperv2.ui.adapter.ItemAdapter;
import com.ristudios.financehelperv2.utils.Utils;

import java.time.ZonedDateTime;

public class MonthlyOverviewActivity extends BaseActivity implements ItemManager.ItemManagerListener, ItemAdapter.AdapterClickListener {

    private ItemManager itemManager;
    private ItemAdapter itemAdapter;
    private int selectedMonth;
    private int selectedYear;
    private int selectedSort; //1: Dateasc, 2: Datedesc, 3: Priceasc, 4:Pricedesc

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_monthly_overview);
        super.onCreate(savedInstanceState);
        initBurgerMenu();
        getSupportActionBar().setTitle(getString(R.string.monthly_overview));
        initData();
        initUi();
    }

    private void initUi() {
        Spinner spnMonthSelector = findViewById(R.id.spn_months);
        Spinner spnYearSelector = findViewById(R.id.spn_years);
        Spinner spnSortMethodSelector = findViewById(R.id.spn_sorting_method);
        RecyclerView recycler = findViewById(R.id.recycler_monthly_overview);
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.setAdapter(itemAdapter);
        spnSortMethodSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: itemManager.sortListByDateDescending();
                            selectedSort= 0;
                        break;
                    case 1: itemManager.sortListByDateAscending();
                        selectedSort= 1;
                        break;
                    case 2: itemManager.sortListByPriceAscending();
                        selectedSort= 2;
                        break;
                    case 3: itemManager.sortListByPriceDescending();
                        selectedSort= 3;
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnMonthSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMonth = position + 1;
                itemManager.loadItemsForMonth(selectedYear, selectedMonth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnMonthSelector.setSelection(selectedMonth - 1);
        spnYearSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYear = position + 2021;
                itemManager.loadItemsForMonth(selectedYear, selectedMonth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnYearSelector.setSelection(selectedYear - 2021);
    }

    private void initData() {
        ZonedDateTime current = Utils.getCurrentZonedTime();
        selectedMonth = getIntent().getIntExtra(Utils.INTENT_KEY_MONTH, current.getMonthValue());
        selectedYear = getIntent().getIntExtra(Utils.INTENT_KEY_YEAR, current.getYear());
        selectedSort = 1;
        itemManager = new ItemManager(this, this);
        itemAdapter = new ItemAdapter(this, this);

    }

    @Override
    public void onItemListUpdated() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateSubTotal();
                itemAdapter.updateList(itemManager.getItems());
                itemAdapter.notifyDataSetChanged();
            }
        });
    }

    private void updateSubTotal() {
        TextView txtSubTotal = findViewById(R.id.txt_total_price);
        txtSubTotal.setText(getString(R.string.currency_sign_value).replace("$VALUE", String.valueOf(itemManager.getTotalPrice())));
    }

    @Override
    public void onListLoaded() {
        switch (selectedSort){
            case 0: itemManager.sortListByDateDescending();
                break;
            case 1: itemManager.sortListByDateAscending();
                break;
            case 2: itemManager.sortListByPriceAscending();
                break;
            case 3: itemManager.sortListByPriceDescending();
                break;

        }
        itemManager.updateData();
    }

    @Override
    public void onItemLayoutClicked(Item item, int pos) {

    }

    @Override
    public void onItemLayoutLongClicked(Item item, int pos) {

    }
}