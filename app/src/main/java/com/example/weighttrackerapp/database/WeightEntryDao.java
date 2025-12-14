package com.example.weighttrackerapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.weighttrackerapp.models.WeightEntry;

import java.util.Date;
import java.util.List;

/**
 * Data Access Object (DAO) for WeightEntry entities.
 */
@Dao
public interface WeightEntryDao {
    
    @Insert
    long insert(WeightEntry weightEntry);
    
    @Update
    void update(WeightEntry weightEntry);
    
    @Delete
    void delete(WeightEntry weightEntry);
    
    @Query("SELECT * FROM weight_entries ORDER BY date DESC")
    LiveData<List<WeightEntry>> getAllWeightEntries();
    
    @Query("SELECT * FROM weight_entries WHERE id = :id")
    LiveData<WeightEntry> getWeightEntryById(int id);
    
    @Query("SELECT * FROM weight_entries ORDER BY date DESC LIMIT 1")
    LiveData<WeightEntry> getLatestWeightEntry();
    
    @Query("SELECT * FROM weight_entries WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    LiveData<List<WeightEntry>> getWeightEntriesBetweenDates(Date startDate, Date endDate);
    
    @Query("SELECT AVG(weight) FROM weight_entries")
    LiveData<Double> getAverageWeight();
    
    @Query("SELECT MAX(weight) FROM weight_entries")
    LiveData<Double> getMaxWeight();
    
    @Query("SELECT MIN(weight) FROM weight_entries")
    LiveData<Double> getMinWeight();
    
    @Query("DELETE FROM weight_entries")
    void deleteAll();
    
    @Query("SELECT COUNT(*) FROM weight_entries")
    LiveData<Integer> getTotalEntriesCount();
}
