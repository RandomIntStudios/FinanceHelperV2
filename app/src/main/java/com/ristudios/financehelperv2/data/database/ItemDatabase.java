package com.ristudios.financehelperv2.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.ristudios.financehelperv2.data.Item;
import com.ristudios.financehelperv2.data.monthlypayments.MonthlyPayment;


/**
 * Represents a database and specifies the DAO.
 */
@Database(version = 2,
        entities = {Item.class, MonthlyPayment.class})
public abstract class ItemDatabase extends RoomDatabase {
    public abstract ItemDAO itemDAO();
    public abstract MonthlyDAO monthlyDAO();


}


