package com.example.weighttrackerapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.weighttrackerapp.database.AppDatabase;
import com.example.weighttrackerapp.models.WeightEntry;

import java.util.List;

/**
 * ViewModel for Weight Tracking Fragment.
 */
public class WeightTrackingViewModel extends AndroidViewModel {
    
    private final AppDatabase database;
    private final LiveData<List<WeightEntry>> allWeightEntries;
    
    public WeightTrackingViewModel(@NonNull Application application) {
        super(application);
        database = AppDatabase.getInstance(application);
        allWeightEntries = database.weightEntryDao().getAllWeightEntries();
    }
    
    public LiveData<List<WeightEntry>> getAllWeightEntries() {
        return allWeightEntries;
    }
    
    public void insertWeightEntry(WeightEntry entry) {
        new Thread(() -> database.weightEntryDao().insert(entry)).start();
    }
    
    public void deleteWeightEntry(WeightEntry entry) {
        new Thread(() -> database.weightEntryDao().delete(entry)).start();
    }
}
