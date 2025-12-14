package com.example.weighttrackerapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.weighttrackerapp.database.AppDatabase;
import com.example.weighttrackerapp.models.Goal;
import com.example.weighttrackerapp.models.WeightEntry;
import com.example.weighttrackerapp.models.UserProfile;

import java.util.Date;

/**
 * ViewModel for Dashboard Fragment.
 */
public class DashboardViewModel extends AndroidViewModel {
    
    private final AppDatabase database;
    private LiveData<WeightEntry> latestWeightEntry;
    private LiveData<Goal> activeGoal;
    private LiveData<Integer> dailyCalories;
    private MutableLiveData<Double> weightChange;
    private UserProfile userProfile;
    
    public DashboardViewModel(@NonNull Application application) {
        super(application);
        database = AppDatabase.getInstance(application);
        latestWeightEntry = database.weightEntryDao().getLatestWeightEntry();
        activeGoal = Transformations.map(database.goalDao().getActiveGoals(), goals -> 
            goals != null && !goals.isEmpty() ? goals.get(0) : null);
        weightChange = new MutableLiveData<>();
        loadUserProfile();
        calculateWeightChange();
    }
    
    public LiveData<WeightEntry> getLatestWeightEntry() {
        return latestWeightEntry;
    }
    
    public LiveData<Goal> getActiveGoal() {
        return activeGoal;
    }
    
    public LiveData<Integer> getDailyCalories() {
        dailyCalories = database.foodEntryDao().getTotalCaloriesByDate(new Date());
        return dailyCalories;
    }
    
    public LiveData<Double> getWeightChange() {
        return weightChange;
    }
    
    private void loadUserProfile() {
        new Thread(() -> {
            userProfile = database.userProfileDao().getUserProfileSync();
        }).start();
    }
    
    private void calculateWeightChange() {
        new Thread(() -> {
            // This would calculate weight change from first entry to latest
            // Simplified for now
            weightChange.postValue(0.0);
        }).start();
    }
    
    public String getUnitPreference() {
        return userProfile != null ? userProfile.getUnitPreference() : "KG";
    }
    
    public double getUserHeight() {
        return userProfile != null ? userProfile.getHeight() : 170.0;
    }
}
