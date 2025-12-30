package com.example.weighttrackerapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.weighttrackerapp.models.WeightEntry;
import com.example.weighttrackerapp.repositories.WeightRepository;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * ViewModel for Analytics Fragment.
 * Handles data transformation for Charts and Statistics.
 */
public class AnalyticsViewModel extends AndroidViewModel {

    private final WeightRepository weightRepository;

    // LiveData derived from the Repository
    private final LiveData<List<Entry>> weightProgressData;
    private final LiveData<Double> averageWeight;
    private final LiveData<Double> weightLoss;
    private final LiveData<String> bmiTrend;

    // Placeholder for now (requires FoodRepository connection)
    private final MutableLiveData<Integer> averageCalories = new MutableLiveData<>(0);

    public AnalyticsViewModel(@NonNull Application application) {
        super(application);
        weightRepository = new WeightRepository(application);

        // 1. CHART DATA: Automatically convert DB WeightEntries -> Chart Entries
        weightProgressData = Transformations.map(weightRepository.getAllWeightEntries(), dbEntries -> {
            List<Entry> chartEntries = new ArrayList<>();
            if (dbEntries != null && !dbEntries.isEmpty()) {
                // Ensure data is sorted by date ascending for the chart
                List<WeightEntry> sortedList = new ArrayList<>(dbEntries);
                Collections.sort(sortedList, Comparator.comparingLong(WeightEntry::getDate));

                for (WeightEntry entry : sortedList) {
                    // X = Timestamp (float), Y = Weight (float)
                    chartEntries.add(new Entry((float) entry.getDate(), (float) entry.getWeight()));
                }
            }
            return chartEntries;
        });

        // 2. AVERAGE WEIGHT
        averageWeight = Transformations.map(weightRepository.getAllWeightEntries(), dbEntries -> {
            if (dbEntries == null || dbEntries.isEmpty()) return 0.0;
            double sum = 0;
            for (WeightEntry w : dbEntries) sum += w.getWeight();
            return sum / dbEntries.size();
        });

        // 3. WEIGHT LOSS (Oldest - Newest)
        weightLoss = Transformations.map(weightRepository.getAllWeightEntries(), dbEntries -> {
            if (dbEntries == null || dbEntries.size() < 2) return 0.0;

            // Sort by date to find oldest and newest accurately
            List<WeightEntry> sortedList = new ArrayList<>(dbEntries);
            Collections.sort(sortedList, Comparator.comparingLong(WeightEntry::getDate));

            double startWeight = sortedList.get(0).getWeight(); // Oldest
            double currentWeight = sortedList.get(sortedList.size() - 1).getWeight(); // Newest

            return startWeight - currentWeight;
        });

        // 4. BMI TREND (Simplified logic)
        bmiTrend = Transformations.map(weightLoss, loss -> {
            if (loss > 0.5) return "Decreasing (Good)";
            if (loss < -0.5) return "Increasing";
            return "Stable";
        });
    }

    // --- Getters ---

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
}