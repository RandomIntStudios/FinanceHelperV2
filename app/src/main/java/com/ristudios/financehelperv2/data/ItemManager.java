package com.ristudios.financehelperv2.data;

import android.content.Context;

import com.ristudios.financehelperv2.data.database.DatabaseExecutor;
import com.ristudios.financehelperv2.data.database.ItemDatabaseHelper;
import com.ristudios.financehelperv2.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    private ItemManagerListener listener;
    private ArrayList<Item> items;
    private DatabaseExecutor databaseExecutor;
    private ItemDatabaseHelper helper;

    public ItemManager (Context context, ItemManagerListener listener)
    {
        this.items = new ArrayList<>();
        this.listener = listener;
        helper = new ItemDatabaseHelper(context);
        databaseExecutor = new DatabaseExecutor(helper);
    }

    public void addItem(Item toAdd){
        items.add(toAdd);
        listener.onItemListUpdated();
        databaseExecutor.databaseAdd(toAdd);
    }

    public void addItemAtPosition(Item toAdd, int pos){
        items.add(pos, toAdd);
        listener.onItemListUpdated();
        databaseExecutor.databaseAdd(toAdd);
    }

    public void updateItem(Item toUpdate, Item updatedItem){
        int pos = items.indexOf(toUpdate);
        removeItem(toUpdate);
        addItemAtPosition(updatedItem, pos);
        listener.onItemListUpdated();

    }

    public void loadItemsForCurrentDate(){
        items.clear();
        long [] searchMillis = Utils.getSearchTimesForCurrentDay();
        databaseExecutor.databaseLoad(searchMillis[0], searchMillis[1], new DatabaseExecutor.DataLoadListener() {
            @Override
            public void onDataLoaded(List<Item> loadedItems) {
                items.addAll(loadedItems);
                listener.onListLoaded();
            }
        });
    }

    public void loadItemsForDate(int year, int monthValue, int day){
        items.clear();
        long[] searchMillis = Utils.getSearchTimesForDate(year, monthValue, day);
        databaseExecutor.databaseLoad(searchMillis[0], searchMillis[1], new DatabaseExecutor.DataLoadListener() {
            @Override
            public void onDataLoaded(List<Item> loadedItems) {
                items.addAll(loadedItems);
                listener.onListLoaded();
            }
        });
    }

    public void loadItemsForMonth(int year, int monthValue){
        items.clear();
        long[] searchMillis = Utils.getSearchTimesForMonth(year, monthValue);
        databaseExecutor.databaseLoad(searchMillis[0], searchMillis[1], new DatabaseExecutor.DataLoadListener() {
            @Override
            public void onDataLoaded(List<Item> loadedItems) {
                items.addAll(loadedItems);
                listener.onListLoaded();
            }
        });
    }

    public void removeItem(Item toRemove){
        Item match = null;
        for(Item item : items){
            if (item.getUuid().equals(toRemove.getUuid())){
                match = item;
            }
        }
        if (match!=null){
            databaseExecutor.databaseDelete(match);
            items.remove(match);
            listener.onItemListUpdated();
        }
    }

    public ArrayList<Item> getItems(){
        return items;
    }

    public void updateData(){
        listener.onItemListUpdated();
    }

    public interface ItemManagerListener{
        void onItemListUpdated();
        void onListLoaded();
    }

}
