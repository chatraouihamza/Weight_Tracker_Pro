package com.example.weighttrackerapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.weighttrackerapp.models.FoodEntry;

import java.util.Date;
import java.util.List;

/**
 * Data Access Object (DAO) for FoodEntry entities.
 */
@Dao
public interface FoodEntryDao {
    
    @Insert
    long insert(FoodEntry foodEntry);
    
    @Update
    void update(FoodEntry foodEntry);
    
    @Delete
    void delete(FoodEntry foodEntry);
    
    @Query("SELECT * FROM food_entries ORDER BY date DESC")
    LiveData<List<FoodEntry>> getAllFoodEntries();
    
    @Query("SELECT * FROM food_entries WHERE id = :id")
    LiveData<FoodEntry> getFoodEntryById(int id);
    
    @Query("SELECT * FROM food_entries WHERE date(date) = date(:date) ORDER BY date DESC")
    LiveData<List<FoodEntry>> getFoodEntriesByDate(Date date);
    
    @Query("SELECT * FROM food_entries WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    LiveData<List<FoodEntry>> getFoodEntriesBetweenDates(Date startDate, Date endDate);
    
    @Query("SELECT SUM(calories) FROM food_entries WHERE date(date) = date(:date)")
    LiveData<Integer> getTotalCaloriesByDate(Date date);
    
    @Query("SELECT SUM(protein) FROM food_entries WHERE date(date) = date(:date)")
    LiveData<Double> getTotalProteinByDate(Date date);
    
    @Query("SELECT SUM(carbs) FROM food_entries WHERE date(date) = date(:date)")
    LiveData<Double> getTotalCarbsByDate(Date date);
    
    @Query("SELECT SUM(fat) FROM food_entries WHERE date(date) = date(:date)")
    LiveData<Double> getTotalFatByDate(Date date);
    
    @Query("DELETE FROM food_entries")
    void deleteAll();
}
