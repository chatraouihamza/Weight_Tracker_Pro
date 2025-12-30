package com.example.weighttrackerapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.weighttrackerapp.models.WeightEntry;

import java.util.List;

@Dao
public interface WeightEntryDao {

    @Insert
    long insert(WeightEntry weightEntry);

    @Update
    void update(WeightEntry weightEntry);

    @Delete
    void delete(WeightEntry weightEntry);

    @Query("SELECT * FROM weight_entries WHERE user_id = :userId ORDER BY date_timestamp DESC")
    LiveData<List<WeightEntry>> getAllWeightEntries(int userId);

    @Query("SELECT * FROM weight_entries WHERE id = :id")
    LiveData<WeightEntry> getWeightEntryById(int id);

    @Query("SELECT * FROM weight_entries WHERE user_id = :userId ORDER BY date_timestamp DESC LIMIT 1")
    LiveData<WeightEntry> getLatestWeightEntry(int userId);

    // Logic matches parameters perfectly now
    @Query("SELECT * FROM weight_entries WHERE user_id = :userId AND date_timestamp BETWEEN :startDate AND :endDate ORDER BY date_timestamp DESC")
    LiveData<List<WeightEntry>> getWeightEntriesBetweenDates(int userId, long startDate, long endDate);

    @Query("SELECT AVG(weight_kg) FROM weight_entries WHERE user_id = :userId")
    LiveData<Double> getAverageWeight(int userId);

    @Query("SELECT MAX(weight_kg) FROM weight_entries WHERE user_id = :userId" )
    LiveData<Double> getMaxWeight(int userId);

    @Query("SELECT MIN(weight_kg) FROM weight_entries WHERE user_id = :userId")
    LiveData<Double> getMinWeight(int userId);

    @Query("DELETE FROM weight_entries WHERE user_id = :userId")
    void deleteAll(int userId);

    @Query("SELECT COUNT(*) FROM weight_entries WHERE user_id = :userId")
    LiveData<Integer> getTotalEntriesCount(int userId);
}