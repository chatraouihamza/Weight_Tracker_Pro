package com.example.weighttrackerapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weighttrackerapp.database.AppDatabase;
import com.github.mikephil.charting.data.Entry;

import java.util.List;

/**
 * ViewModel for Analytics Fragment.
 */
public class AnalyticsViewModel extends AndroidViewModel {
    
    private final AppDatabase database;
    private final MutableLiveData<List<Entry>> weightProgressData;
    private final MutableLiveData<Double> averageWeight;
    private final MutableLiveData<Double> weightLoss;
    private final MutableLiveData<String> bmiTrend;
    private final MutableLiveData<Integer> averageCalories;
    
    public AnalyticsViewModel(@NonNull Application application) {
        super(application);
        database = AppDatabase.getInstance(application);
        weightProgressData = new MutableLiveData<>();
        averageWeight = new MutableLiveData<>();
        weightLoss = new MutableLiveData<>();
        bmiTrend = new MutableLiveData<>();
        averageCalories = new MutableLiveData<>();
        loadAnalyticsData();
    }
    
    public LiveData<List<Entry>> getWeightProgressData() {
        return weightProgressData;
    }
    
    public LiveData<Double> getAverageWeight() {
        return averageWeight;
    }
    
    public LiveData<Double> getWeightLoss() {
        return weightLoss;
    }
    
    public LiveData<String> getBMITrend() {
        return bmiTrend;
    }
    
    public LiveData<Integer> getAverageCalories() {
        return averageCalories;
    }
    
    private void loadAnalyticsData() {
        new Thread(() -> {
            // Load weight progress data
            // Calculate average weight
            // Calculate weight loss
            // Determine BMI trend
            // Calculate average calories
        }).start();
    }
}
