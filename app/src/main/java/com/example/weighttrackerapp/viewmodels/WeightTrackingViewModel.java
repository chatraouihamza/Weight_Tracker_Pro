package com.example.weighttrackerapp.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.weighttrackerapp.models.Goal;
import com.example.weighttrackerapp.repositories.GoalRepository;
import com.example.weighttrackerapp.models.WeightEntry;
import com.example.weighttrackerapp.repositories.WeightRepository;

import com.example.weighttrackerapp.models.Measurement;
import com.example.weighttrackerapp.repositories.MeasurementRepository;

import java.util.List;



public class WeightTrackingViewModel extends AndroidViewModel {

    private final WeightRepository weightRepository;
    private final GoalRepository goalRepository;
    private final MeasurementRepository measurementRepository;

    private final LiveData<List<WeightEntry>> allWeightEntries;
    private final LiveData<Goal> activeGoal;
    private final LiveData<Measurement> latestMeasurement;

    public WeightTrackingViewModel(@NonNull Application application) {
        super(application);
        weightRepository = new WeightRepository(application);
        goalRepository = new GoalRepository(application);
        measurementRepository = new MeasurementRepository(application);


        allWeightEntries = weightRepository.getAllWeightEntries();

        // Get the first active goal to draw the prediction line
        activeGoal = Transformations.map(goalRepository.getActiveGoals(), goals -> {
            if (goals != null && !goals.isEmpty()) {
                return goals.get(0);
            }
            return null;
        });

        latestMeasurement = measurementRepository.getLatestMeasurement();

    }

    // --- Data Getters ---
    public LiveData<List<WeightEntry>> getAllWeightEntries() {
        return allWeightEntries;
    }

    public LiveData<Goal> getActiveGoal() {
        return activeGoal;
    }

    public LiveData<Measurement> getLatestMeasurement() {
        return latestMeasurement;
    }

    // --- User Actions ---
    public void addNewWeight(double weight, long date, String note) {
        WeightEntry entry = new WeightEntry(weight, date);
        weightRepository.insert(entry);

        // 1. Insert Weight
        weightRepository.insert(entry);

        // 2. TRIGGER GOAL UPDATE
        goalRepository.updateGoalsProgress(weight);
    }

    public void addMeasurement(double waist, double hips, double bodyFat) {
        // Create measurement (using current time)
        Measurement m = new Measurement();
        m.setDate(System.currentTimeMillis());
        m.setWaist(waist);
        m.setHips(hips);
        m.setBodyFat(bodyFat);
        // Note: userId is handled inside the Repository
        measurementRepository.insert(m);
    }

}