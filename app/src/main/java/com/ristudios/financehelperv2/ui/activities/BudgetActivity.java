package com.ristudios.financehelperv2.ui.activities;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ristudios.financehelperv2.R;
import com.ristudios.financehelperv2.data.Item;
import com.ristudios.financehelperv2.data.ItemManager;
import com.ristudios.financehelperv2.ui.adapter.ItemAdapter;
import com.ristudios.financehelperv2.ui.fragments.AddOrUpdateDialog;
import com.ristudios.financehelperv2.ui.fragments.DeleteDialog;
import com.ristudios.financehelperv2.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public class BudgetActivity extends BaseActivity implements ItemManager.ItemManagerListener, ItemAdapter.AdapterClickListener, AddOrUpdateDialog.AddOrUpdateDialogListener, DeleteDialog.DeleteDialogListener {

    private ItemManager itemManager;
    private TextView txtDate, txtSubTotalValue, txtRemainingBudget;
    private ItemAdapter itemAdapter;
    private FloatingActionButton fabAddItem;
    private float maximumBudget, currentBudget;
    private SharedPreferences preferences;
    private ProgressBar pbrBudgetRemaining;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_budget);
        super.onCreate(savedInstanceState);
        initBurgerMenu();
        getSupportActionBar().setTitle(R.string.budget);
        initData();
        initUI();
        itemManager.loadItemsForCurrentDate();
    }

    private void initUI() {
        iconDelete = Utils.drawableToBitmap(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_delete_sweep_24, null));
        txtDate = findViewById(R.id.txt_date);
        txtDate.setText(Utils.getLocalizedFormattedDate(LocalDate.now()));
        txtSubTotalValue = findViewById(R.id.txt_total_price);
        txtRemainingBudget = findViewById(R.id.txt_remaining_budget);
        txtRemainingBudget.setText(getResources().getString(R.string.currency_sign_value).replace("$VALUE", String.valueOf(currentBudget)));
        RecyclerView recyclerView = findViewById(R.id.recycler_items);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        fabAddItem = findViewById(R.id.fab_add_item);
        fabAddItem.setOnClickListener(v -> {
            AddOrUpdateDialog dialog = new AddOrUpdateDialog();
            dialog.setMode(AddOrUpdateDialog.MODE_NEW);
            dialog.show(getSupportFragmentManager(), "some tag");
        });
        pbrBudgetRemaining = findViewById(R.id.pbr_remaining_budget);
        pbrBudgetRemaining.setMax(Math.round(maximumBudget));
        pbrBudgetRemaining.setMin(0);
        pbrBudgetRemaining.setProgress(Math.round(currentBudget));
    }


    private void initData() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        itemManager = new ItemManager(getApplicationContext(), this);
        itemAdapter = new ItemAdapter(this, getApplicationContext());
        currentBudget = preferences.getFloat(Utils.PREFS_CURRENT_BUDGET_KEY, 200);
        maximumBudget = preferences.getFloat(Utils.PREFS_MAXIMUM_BUDGET_KEY, 200);
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
        txtSubTotalValue.setText(getResources().getString(R.string.currency_sign_value).replace("$VALUE", String.valueOf(total)));
    }




    @Override
    public void onItemListUpdated() {
        runOnUiThread(() -> {
            updateSubTotal();
            itemAdapter.updateList(itemManager.getItems());
            itemAdapter.notifyDataSetChanged();
            pbrBudgetRemaining.setProgress(Math.round(currentBudget));
            txtRemainingBudget.setText(getResources().getString(R.string.currency_sign_value).replace("$VALUE", String.valueOf(currentBudget)));
        });
    }

    @Override
    public void onListLoaded() {
        itemManager.updateData();
    }

    @Override
    public void onItemLayoutClicked(Item item, int pos) {
        AddOrUpdateDialog dialog = new AddOrUpdateDialog();
        dialog.setMode(AddOrUpdateDialog.MODE_UPDATE);
        dialog.setItem(item);
        dialog.show(getSupportFragmentManager(), "some tag");
    }

    @Override
    public void onItemLayoutLongClicked(Item item, int pos) {
        DeleteDialog deleteDialog = new DeleteDialog();
        deleteDialog.setItem(item);
        deleteDialog.show(getSupportFragmentManager(), "someTag");
    }

    @Override
    public void onItemNew(Item newItem) {
        if (newItem.isIncome()){
            currentBudget = currentBudget + newItem.getPriceTotal();
        }else {
            currentBudget = currentBudget - newItem.getPriceTotal();
        }
        currentBudget = Utils.roundToTwoDecimals(currentBudget);
        preferences.edit().putFloat(Utils.PREFS_CURRENT_BUDGET_KEY, currentBudget).apply();
        itemManager.addItem(newItem);

    }

    @Override
    public void onItemUpdate(Item toUpdate, Item updatedItem) {
        if (toUpdate.isIncome()){
            currentBudget = currentBudget + toUpdate.getPriceTotal();
        }
        else{
            currentBudget = currentBudget - toUpdate.getPriceTotal();
        }
        if (updatedItem.isIncome()){
            currentBudget = currentBudget + updatedItem.getPriceTotal();
        }
        else{
            currentBudget = currentBudget - updatedItem.getPriceTotal();
        }
        currentBudget = Utils.roundToTwoDecimals(currentBudget);
        preferences.edit().putFloat(Utils.PREFS_CURRENT_BUDGET_KEY, currentBudget).apply();
        itemManager.updateItem(toUpdate, updatedItem);
    }

    @Override
    public void onNegativeClicked() {
        Toast.makeText(this, getResources().getString(R.string.toast_discarded), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoClicked() {
        Toast.makeText(this, getResources().getString(R.string.price_info), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeleteClicked(Item toDelete) {
        if (toDelete.isIncome()){
            currentBudget = currentBudget - toDelete.getPriceTotal();
        }else {
            currentBudget = currentBudget + toDelete.getPriceTotal();
        }
        currentBudget = Utils.roundToTwoDecimals(currentBudget);
        preferences.edit().putFloat(Utils.PREFS_CURRENT_BUDGET_KEY, currentBudget).apply();
        itemManager.removeItem(toDelete);
    }
}