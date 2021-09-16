package com.ristudios.financehelperv2.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ristudios.financehelperv2.data.Item;

import java.util.List;

@Dao
public interface ItemDAO {

    @Insert
    void insertItem(Item item);

    @Update
    void updateItem(Item item);

    @Delete
    void deleteItem(Item item);

    @Query("SELECT * from items WHERE dateMillis>= :start AND dateMillis <= :end")
    List<Item> getItemsForTime(long start, long end);

    @Query("SELECT * from items")
    List<Item> getAllItems();

}
