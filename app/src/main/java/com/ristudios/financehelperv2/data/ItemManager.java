package com.ristudios.financehelperv2.data;

import android.content.Context;

import com.ristudios.financehelperv2.data.database.DatabaseExecutor;
import com.ristudios.financehelperv2.data.database.ItemDatabaseHelper;
import com.ristudios.financehelperv2.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds a List containing Items.
 */
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

    public void clearViews(){
        items.clear();
        listener.onItemListUpdated();
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

    public void sortListByPriceAscending() {
        items = Utils.sortListByPrice(items);
        listener.onItemListUpdated();
    }

    public void sortListByPriceDescending(){
        items = Utils.sortListByPrice(items);
        Collections.reverse(items);
        listener.onItemListUpdated();
    }

    public void sortListByDateDescending(){
        items = Utils.sortListByDate(items);
        listener.onItemListUpdated();
    }

    public void sortListByDateAscending(){
        items = Utils.sortListByDate(items);
        Collections.reverse(items);
        listener.onItemListUpdated();
    }

    public void loadItemsForCurrentDate(){
        long [] searchMillis = Utils.getSearchTimesForCurrentDay();
        databaseExecutor.databaseLoadForTime(searchMillis[0], searchMillis[1], new DatabaseExecutor.DataLoadListener() {
            @Override
            public void onDataLoaded(List<Item> loadedItems) {
                items.clear();
                items.addAll(loadedItems);
                listener.onListLoaded();
            }
        });
    }

    public void loadItemsForDate(int year, int monthValue, int day){
        long[] searchMillis = Utils.getSearchTimesForDate(year, monthValue, day);
        databaseExecutor.databaseLoadForTime(searchMillis[0], searchMillis[1], new DatabaseExecutor.DataLoadListener() {
            @Override
            public void onDataLoaded(List<Item> loadedItems) {
                items.clear();
                items.addAll(loadedItems);
                listener.onListLoaded();
            }
        });
    }

    public void loadItemsForMonth(int year, int monthValue){
        long[] searchMillis = Utils.getSearchTimesForMonth(year, monthValue);
        databaseExecutor.databaseLoadForTime(searchMillis[0], searchMillis[1], new DatabaseExecutor.DataLoadListener() {
            @Override
            public void onDataLoaded(List<Item> loadedItems) {
                items.clear();
                items.addAll(loadedItems);
                listener.onListLoaded();
            }
        });
    }


    public float getTotalPrice(){
        float total = 0;
        for (Item item: items){
            if (item.isIncome()){
                total = total - item.getPriceTotal();
            }
            else{
                total = total + item.getPriceTotal();
            }
        }
        return total;
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
