package com.example.weighttrackerapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.weighttrackerapp.models.Goal;

import java.util.List;

/**
 * Data Access Object (DAO) for Goal entities.
 */
@Dao
public interface GoalDao {
    
    @Insert
    long insert(Goal goal);
    
    @Update
    void update(Goal goal);
    
    @Delete
    void delete(Goal goal);
    
    @Query("SELECT * FROM goals ORDER BY targetDate ASC")
    LiveData<List<Goal>> getAllGoals();
    
    @Query("SELECT * FROM goals WHERE id = :id")
    LiveData<Goal> getGoalById(int id);
    
    @Query("SELECT * FROM goals WHERE isCompleted = 0 ORDER BY targetDate ASC")
    LiveData<List<Goal>> getActiveGoals();
    
    @Query("SELECT * FROM goals WHERE isCompleted = 1 ORDER BY completedDate DESC")
    LiveData<List<Goal>> getCompletedGoals();
    
    @Query("DELETE FROM goals")
    void deleteAll();
    
    @Query("SELECT COUNT(*) FROM goals WHERE isCompleted = 0")
    LiveData<Integer> getActiveGoalsCount();
}
