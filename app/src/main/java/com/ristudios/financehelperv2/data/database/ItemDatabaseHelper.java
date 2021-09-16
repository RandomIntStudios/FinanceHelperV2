package com.ristudios.financehelperv2.data.database;

import android.content.Context;

import androidx.room.Room;

import com.ristudios.financehelperv2.data.Item;

import java.util.ArrayList;

public class ItemDatabaseHelper {

    private static final String DATABASE_NAME = "ristudios.financehelperv2:itemdatabase";
    private final ItemDatabase database;

    public ItemDatabaseHelper(Context context){
        database = Room.databaseBuilder(context, ItemDatabase.class, DATABASE_NAME).build();
    }

    public void addItem(Item item){
        database.itemDAO().insertItem(item);
    }

    public void updateItem(Item item){
        database.itemDAO().updateItem(item);
    }

    public void deleteItem(Item item){
        database.itemDAO().deleteItem(item);
    }

    public ArrayList<Item> getItemsForTime(long start, long end){
        return new ArrayList<>(database.itemDAO().getItemsForTime(start, end));
    }

    public ArrayList<Item> getAllItems()
    {
        return new ArrayList<>(database.itemDAO().getAllItems());
    }
}
