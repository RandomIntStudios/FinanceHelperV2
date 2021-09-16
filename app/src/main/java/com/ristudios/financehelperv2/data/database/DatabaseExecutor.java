package com.ristudios.financehelperv2.data.database;

import com.ristudios.financehelperv2.data.Item;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DatabaseExecutor {
    private final ItemDatabaseHelper helper;

    public DatabaseExecutor (ItemDatabaseHelper helper)
    {
        this.helper = helper;
    }

    public void databaseAdd(Item item){
        Executor e = Executors.newSingleThreadExecutor();
        e.execute(() -> {
            helper.addItem(item);
        });
    }

    public void databaseUpdate(Item item){
        Executor e = Executors.newSingleThreadExecutor();
        e.execute(() -> {
            helper.updateItem(item);
        });
    }

    public void databaseDelete(Item item){
        Executor e = Executors.newSingleThreadExecutor();
        e.execute(() -> {
            helper.deleteItem(item);
        });
    }

    public void databaseLoad(long start, long end, DataLoadListener listener){
        Executor e = Executors.newSingleThreadExecutor();
        e.execute(() -> listener.onDataLoaded(helper.getItemsForTime(start, end)));
    }

    public interface DataLoadListener{
        void onDataLoaded(List<Item> loadedItems);
    }
}
