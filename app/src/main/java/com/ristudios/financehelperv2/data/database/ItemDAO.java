package com.ristudios.financehelperv2.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ristudios.financehelperv2.data.Item;

import java.util.List;

/**
 * Data Access Object for database operations.
 */
@Dao
public interface ItemDAO {

    /**
     * Inserts an item into the item database table.
     * @param item
     */
    @Insert
    void insertItem(Item item);

    /**
     * Updates an item in the item database table.
     * @param item
     */
    @Update
    void updateItem(Item item);

    /**
     * Deletes an item from the item database table.
     * @param item The item to delete.
     */
    @Delete
    void deleteItem(Item item);

    /**
     * Gets every object from the item database table in a specific time span.
     * @param start start of time span.
     * @param end end of time span.
     * @return List of all items matching the given parameters.
     */
    @Query("SELECT * from items WHERE dateMillis>= :start AND dateMillis <= :end")
    List<Item> getItemsForTime(long start, long end);

    /**
     * Gets all objects from the item database table.
     * @return List containing every db entry.
     */
    @Query("SELECT * from items")
    List<Item> getAllItems();

}
