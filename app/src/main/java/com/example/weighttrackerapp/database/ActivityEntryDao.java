package com.example.weighttrackerapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.weighttrackerapp.models.ActivityEntry;

import java.util.Date;
import java.util.List;

/**
 * Data Access Object (DAO) for ActivityEntry entities.
 */
@Dao
public interface ActivityEntryDao {
    
    @Insert
    long insert(ActivityEntry activityEntry);
    
    @Update
    void update(ActivityEntry activityEntry);
    
    @Delete
    void delete(ActivityEntry activityEntry);
    
    @Query("SELECT * FROM activity_entries ORDER BY date DESC")
    LiveData<List<ActivityEntry>> getAllActivityEntries();
    
    @Query("SELECT * FROM activity_entries WHERE id = :id")
    LiveData<ActivityEntry> getActivityEntryById(int id);
    
    @Query("SELECT * FROM activity_entries WHERE date(date) = date(:date) ORDER BY date DESC")
    LiveData<List<ActivityEntry>> getActivityEntriesByDate(Date date);
    
    @Query("SELECT * FROM activity_entries WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    LiveData<List<ActivityEntry>> getActivityEntriesBetweenDates(Date startDate, Date endDate);
    
    @Query("SELECT SUM(caloriesBurned) FROM activity_entries WHERE date(date) = date(:date)")
    LiveData<Integer> getTotalCaloriesBurnedByDate(Date date);
    
    @Query("SELECT SUM(duration) FROM activity_entries WHERE date(date) = date(:date)")
    LiveData<Integer> getTotalDurationByDate(Date date);
    
    @Query("DELETE FROM activity_entries")
    void deleteAll();
}
