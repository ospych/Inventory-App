package com.example.inventoryapp.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ItemDao {
    @Insert
    void insert(ItemDb itemDb);

    @Update
    void update(ItemDb itemDb);

    @Delete
    void delete(ItemDb itemDb);

    @Query("DELETE FROM item_table")
    void deleteAllItems();


    @Query("SELECT * FROM item_table ORDER BY quantity DESC")
    LiveData<List<ItemDb>> getAllItems();
}
