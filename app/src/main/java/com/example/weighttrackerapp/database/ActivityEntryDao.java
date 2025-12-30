package com.example.weighttrackerapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.weighttrackerapp.models.ActivityEntry;

import java.util.List;

@Dao
public interface ActivityEntryDao {

    @Insert
    long insert(ActivityEntry activityEntry);

    @Update
    void update(ActivityEntry activityEntry);

    @Delete
    void delete(ActivityEntry activityEntry);

    @Query("SELECT * FROM activity_entries WHERE user_id = :userId ORDER BY date_timestamp DESC")
    LiveData<List<ActivityEntry>> getAllActivityEntries(int userId);

    @Query("SELECT * FROM activity_entries WHERE id = :id")
    LiveData<ActivityEntry> getActivityEntryById(int id);

    @Query("SELECT * FROM activity_entries WHERE user_id = :userId AND date_timestamp BETWEEN :startTime AND :endTime ORDER BY date_timestamp DESC")
    LiveData<List<ActivityEntry>> getActivityEntriesForDay(int userId, long startTime, long endTime);

    @Query("SELECT SUM(calories_burned) FROM activity_entries WHERE user_id = :userId AND date_timestamp BETWEEN :startTime AND :endTime")
    LiveData<Integer> getTotalCaloriesBurnedForDay(int userId, long startTime, long endTime);

    @Query("SELECT SUM(duration) FROM activity_entries WHERE user_id = :userId AND date_timestamp BETWEEN :startTime AND :endTime")
    LiveData<Integer> getTotalDurationForDay(int userId, long startTime, long endTime);

    @Query("DELETE FROM activity_entries WHERE user_id = :userId")
    void deleteAll(int userId);
}