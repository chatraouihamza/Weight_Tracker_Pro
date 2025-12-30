package com.example.weighttrackerapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.weighttrackerapp.models.Goal;

import java.util.List;

@Dao
public interface GoalDao {

    @Insert
    void insert(Goal goal);

    @Update
    void update(Goal goal);

    @Delete
    void delete(Goal goal);

    @Query("SELECT * FROM goals WHERE user_id = :userId ORDER BY target_date ASC")
    LiveData<List<Goal>> getAllGoals(int userId);

    @Query("SELECT * FROM goals WHERE id = :id")
    LiveData<Goal> getGoalById(int id); // ID is unique globally, so strictly no need for user_id here, but good practice to verify ownership logic layer

    @Query("SELECT * FROM goals WHERE user_id = :userId AND is_completed = 0 ORDER BY target_date ASC")
    LiveData<List<Goal>> getActiveGoals(int userId);

    @Query("SELECT * FROM goals WHERE user_id = :userId AND is_completed = 1 ORDER BY target_date DESC")
    LiveData<List<Goal>> getCompletedGoals(int userId);

    @Query("DELETE FROM goals WHERE user_id = :userId")
    void deleteAll(int userId);

    @Query("SELECT COUNT(*) FROM goals WHERE user_id = :userId AND is_completed = 0")
    LiveData<Integer> getActiveGoalsCount(int userId);

    @Query("SELECT * FROM goals WHERE user_id = :userId AND is_completed = 0")
    List<Goal> getActiveGoalsSync(int userId);
}