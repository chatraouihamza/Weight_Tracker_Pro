package com.example.weighttrackerapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.weighttrackerapp.database.AppDatabase;
import com.example.weighttrackerapp.models.Goal;

import java.util.List;

/**
 * ViewModel for Goals Fragment.
 */
public class GoalsViewModel extends AndroidViewModel {
    
    private final AppDatabase database;
    private final LiveData<List<Goal>> activeGoals;
    private final LiveData<List<Goal>> completedGoals;
    
    public GoalsViewModel(@NonNull Application application) {
        super(application);
        database = AppDatabase.getInstance(application);
        activeGoals = database.goalDao().getActiveGoals();
        completedGoals = database.goalDao().getCompletedGoals();
    }
    
    public LiveData<List<Goal>> getActiveGoals() {
        return activeGoals;
    }
    
    public LiveData<List<Goal>> getCompletedGoals() {
        return completedGoals;
    }
    
    public void insertGoal(Goal goal) {
        new Thread(() -> database.goalDao().insert(goal)).start();
    }
    
    public void updateGoal(Goal goal) {
        new Thread(() -> database.goalDao().update(goal)).start();
    }
    
    public void deleteGoal(Goal goal) {
        new Thread(() -> database.goalDao().delete(goal)).start();
    }
}
