package com.example.weighttrackerapp.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.weighttrackerapp.database.AppDatabase;
import com.example.weighttrackerapp.database.GoalDao;
import com.example.weighttrackerapp.models.Goal;
import com.example.weighttrackerapp.utils.SessionManager;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GoalRepository {

    private final GoalDao goalDao;
    private final int currentUserId;
    private final ExecutorService executorService;

    public GoalRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        goalDao = db.goalDao();
        executorService = Executors.newFixedThreadPool(4);

        SessionManager session = new SessionManager(application);
        currentUserId = session.getUserId();
    }

    public LiveData<List<Goal>> getActiveGoals() {
        return goalDao.getActiveGoals(currentUserId);
    }

    public LiveData<List<Goal>> getCompletedGoals() {
        return goalDao.getCompletedGoals(currentUserId);
    }

    public void insert(Goal goal) {
        goal.setUserId(currentUserId);
        executorService.execute(() -> goalDao.insert(goal));
    }

    public void update(Goal goal) {
        goal.setUserId(currentUserId);
        executorService.execute(() -> goalDao.update(goal));
    }

    public void delete(Goal goal) {
        executorService.execute(() -> goalDao.delete(goal));
    }

    public void updateGoalsProgress(double currentWeight) {
        executorService.execute(() -> {
            // 1. Get all active goals for this user
            List<Goal> activeGoals = goalDao.getActiveGoalsSync(currentUserId);

            if (activeGoals != null && !activeGoals.isEmpty()) {
                for (Goal goal : activeGoals) {
                    // 2. Calculate new progress using the Entity's logic
                    goal.updateProgress(currentWeight);

                    // 3. Update the DB
                    goalDao.update(goal);
                }
            }
        });
    }
}