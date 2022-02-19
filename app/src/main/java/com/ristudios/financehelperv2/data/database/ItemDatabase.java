package com.ristudios.financehelperv2.data.database;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.ristudios.financehelperv2.data.Item;
import com.ristudios.financehelperv2.data.MonthlyPayment;


/**
 * Represents a database and specifies the DAO.
 */
@androidx.room.TypeConverters(TypeConverters.class)
@Database(version = 2,
        entities = {Item.class, MonthlyPayment.class})
public abstract class ItemDatabase extends RoomDatabase {
    public abstract ItemDAO itemDAO();


}


