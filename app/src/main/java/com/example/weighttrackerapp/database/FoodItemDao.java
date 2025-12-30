package com.example.weighttrackerapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.weighttrackerapp.models.FoodItem;
import java.util.List;

@Dao
public interface FoodItemDao {
    @Insert
    void insert(FoodItem item);

    @Query("DELETE FROM food_items")
    void deleteAll();

    // Search Query
    @Query("SELECT * FROM food_items WHERE name LIKE '%' || :searchQuery || '%' ORDER BY name ASC")
    LiveData<List<FoodItem>> searchFood(String searchQuery);
}