package com.example.weighttrackerapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.weighttrackerapp.models.FoodEntry;

import java.util.List;

@Dao
public interface FoodEntryDao {

    @Insert
    void insert(FoodEntry foodEntry);

    @Update
    void update(FoodEntry foodEntry);

    @Delete
    void delete(FoodEntry foodEntry);

    // Get entries for a specific day range AND specific user
    @Query("SELECT * FROM food_entries WHERE user_id = :userId AND date_timestamp BETWEEN :startTime AND :endTime ORDER BY date_timestamp DESC")
    LiveData<List<FoodEntry>> getFoodEntriesForDay(int userId, long startTime, long endTime);

    // Sum macros for a specific day range AND specific user
    @Query("SELECT SUM(calories) FROM food_entries WHERE user_id = :userId AND date_timestamp BETWEEN :startTime AND :endTime")
    LiveData<Integer> getTotalCaloriesForDay(int userId, long startTime, long endTime);

    @Query("SELECT SUM(protein) FROM food_entries WHERE user_id = :userId AND date_timestamp BETWEEN :startTime AND :endTime")
    LiveData<Double> getTotalProteinForDay(int userId, long startTime, long endTime);

    @Query("SELECT SUM(carbs) FROM food_entries WHERE user_id = :userId AND date_timestamp BETWEEN :startTime AND :endTime")
    LiveData<Double> getTotalCarbsForDay(int userId, long startTime, long endTime);

    @Query("SELECT SUM(fat) FROM food_entries WHERE user_id = :userId AND date_timestamp BETWEEN :startTime AND :endTime")
    LiveData<Double> getTotalFatForDay(int userId, long startTime, long endTime);

    @Query("DELETE FROM food_entries WHERE user_id = :userId")
    void deleteAll(int userId);
}