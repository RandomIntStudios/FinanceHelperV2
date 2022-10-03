package com.ristudios.financehelperv2.data.database;

import com.ristudios.financehelperv2.data.Item;
import com.ristudios.financehelperv2.data.monthlypayments.MonthlyPayment;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Takes care of threading when working with databases and executes the database operations.
 */
public class DatabaseExecutor {
    private final ItemDatabaseHelper helper;

    public DatabaseExecutor (ItemDatabaseHelper helper)
    {
        this.helper = helper;
    }

    public void databaseItemsAdd(Item item){
        Executor e = Executors.newSingleThreadExecutor();
        e.execute(() -> {
            helper.addItem(item);
        });
    }

    public void databaseItemsLoad(ItemDataLoadListener loadListener){
        Executor e = Executors.newSingleThreadExecutor();
        e.execute(() -> {
            loadListener.onItemsLoaded(helper.getAllItems());
        });
    }

    public void databaseUpdate(Item item){
        Executor e = Executors.newSingleThreadExecutor();
        e.execute(() -> {
            helper.updateItem(item);
        });
    }

    public void databaseItemsDelete(Item item){
        Executor e = Executors.newSingleThreadExecutor();
        e.execute(() -> {
            helper.deleteItem(item);
        });
    }

    public void databaseItemsLoadForTime(long start, long end, ItemDataLoadListener listener){
        Executor e = Executors.newSingleThreadExecutor();
        e.execute(() -> listener.onItemsLoaded(helper.getItemsForTime(start, end)));
    }

    public void databaseMonthlyPaymentsAdd(MonthlyPayment monthlyPayment){
        Executor e = Executors.newSingleThreadExecutor();
        e.execute(() -> {
            helper.addMonthlyPayment(monthlyPayment);
        });
    }

    public void databaseMonthlyPaymentsDelete(MonthlyPayment monthlyPayment){
        Executor e = Executors.newSingleThreadExecutor();
        e.execute(() -> {
            helper.deleteMonthlyPayment(monthlyPayment);
        });
    }

    public void databaseMonthlyPaymentsLoadAll(MonthlyPaymentDataLoadListener listener){
        Executor e = Executors.newSingleThreadExecutor();
        e.execute(() -> {
            listener.onPaymentsLoaded(helper.getAllMonthlyPayments());
        });
    }

    public interface ItemDataLoadListener {
        void onItemsLoaded(List<Item> loadedItems);
    }

    public interface MonthlyPaymentDataLoadListener {
        void onPaymentsLoaded(List<MonthlyPayment> loadedPayments);
    }
}
