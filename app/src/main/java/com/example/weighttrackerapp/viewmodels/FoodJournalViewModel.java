package com.example.weighttrackerapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weighttrackerapp.database.AppDatabase;
import com.example.weighttrackerapp.models.FoodEntry;

import java.util.Date;
import java.util.List;

/**
 * ViewModel for Food Journal Fragment.
 */
public class FoodJournalViewModel extends AndroidViewModel {
    
    private final AppDatabase database;
    private final MutableLiveData<List<FoodEntry>> todayFoodEntries;
    private final MutableLiveData<Integer> dailyCalories;
    private final MutableLiveData<double[]> dailyMacros;
    
    public FoodJournalViewModel(@NonNull Application application) {
        super(application);
        database = AppDatabase.getInstance(application);
        todayFoodEntries = new MutableLiveData<>();
        dailyCalories = new MutableLiveData<>();
        dailyMacros = new MutableLiveData<>();
        loadTodayData();
    }
    
    public LiveData<List<FoodEntry>> getTodayFoodEntries() {
        return todayFoodEntries;
    }
    
    public LiveData<Integer> getDailyCalories() {
        return dailyCalories;
    }
    
    public LiveData<double[]> getDailyMacros() {
        return dailyMacros;
    }
    
    public void insertFoodEntry(FoodEntry entry) {
        new Thread(() -> {
            database.foodEntryDao().insert(entry);
            loadTodayData();
        }).start();
    }
    
    public void deleteFoodEntry(FoodEntry entry) {
        new Thread(() -> {
            database.foodEntryDao().delete(entry);
            loadTodayData();
        }).start();
    }
    
    private void loadTodayData() {
        new Thread(() -> {
            Date today = new Date();
            // Load today's food entries
            // Load daily calories
            // Load daily macros
        }).start();
    }
}
