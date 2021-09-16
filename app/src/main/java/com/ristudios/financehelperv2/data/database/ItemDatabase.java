package com.ristudios.financehelperv2.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.ristudios.financehelperv2.data.Item;

@Database(entities = {Item.class}, version = 1)
public abstract class ItemDatabase extends RoomDatabase {
    public abstract ItemDAO itemDAO();
}
