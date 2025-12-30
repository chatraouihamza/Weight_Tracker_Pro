package com.example.weighttrackerapp.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.weighttrackerapp.models.Goal;
import com.example.weighttrackerapp.models.WeightEntry;
import com.example.weighttrackerapp.repositories.GoalRepository;
import com.example.weighttrackerapp.repositories.WeightRepository;

import java.util.List;

public class GoalsViewModel extends AndroidViewModel {

    private final GoalRepository repository;
    private final LiveData<List<Goal>> activeGoals;
    private final LiveData<List<Goal>> completedGoals;
    private final WeightRepository weightRepository;


    public GoalsViewModel(@NonNull Application application) {
        super(application);
        repository = new GoalRepository(application);
        activeGoals = repository.getActiveGoals();
        completedGoals = repository.getCompletedGoals();
        weightRepository = new WeightRepository(application);
    }

    public LiveData<List<Goal>> getActiveGoals() {
        return activeGoals;
    }

    public LiveData<List<Goal>> getCompletedGoals() {
        return completedGoals;
    }

    public void addNewGoal(Goal goal) {
        repository.insert(goal);
    }

    public void updateGoal(Goal goal) {
        repository.update(goal);
    }

    public void deleteGoal(Goal goal) {
        repository.delete(goal);
    }

    public LiveData<WeightEntry> getLatestWeight() {
        return weightRepository.getLatestWeightEntry();
    }

}