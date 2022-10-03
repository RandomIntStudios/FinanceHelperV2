package com.ristudios.financehelperv2.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.ristudios.financehelperv2.data.Item;
import com.ristudios.financehelperv2.data.monthlypayments.MonthlyPayment;

import java.util.ArrayList;

/**
 * Functions as an access class to the item database.
 */
public class ItemDatabaseHelper {

    private static final String DATABASE_NAME = "ristudios.financehelperv2:itemdatabase";
    private final ItemDatabase database;

    /**
     * Migration from Version 1 to 2.
     * Creating monthlyPayments table with id, name, price and category columns.
     * Altering items table and adding column for category.
     */
    Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE 'monthlyPayments' ('uuid' TEXT NOT NULL," +
                    " 'name' TEXT," +
                    " 'price' REAL NOT NULL DEFAULT 0," +
                    " 'category' TEXT," +
                    " PRIMARY KEY('uuid'))");

            database.execSQL("ALTER TABLE items " +
                    " ADD COLUMN category TEXT ");

        }
    };

    /**
     * Builds a new ItemDatabaseHelper and creates the database.
     * @param context Application context.
     */
    public ItemDatabaseHelper(Context context){
        database = Room.databaseBuilder(context, ItemDatabase.class, DATABASE_NAME).addMigrations(MIGRATION_1_2).build();
    }

    /**
     * Adds an item to the bd by calling the dao method.
     * @param item The item to insert.
     */
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

    public void addMonthlyPayment(MonthlyPayment payment) {
        database.monthlyDAO().insertMonthlyPayment(payment);
    }

    public void deleteMonthlyPayment(MonthlyPayment payment) {
        database.monthlyDAO().deleteMonthlyPayment(payment);
    }

    public ArrayList<MonthlyPayment> getAllMonthlyPayments()
    {
        return new ArrayList<>(database.monthlyDAO().getAllMonthlyPayments());
    }
}
