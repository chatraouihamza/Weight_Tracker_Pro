package com.example.weighttrackerapp.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.weighttrackerapp.models.Goal;
import com.example.weighttrackerapp.models.UserProfile;
import com.example.weighttrackerapp.models.WeightEntry;
import com.example.weighttrackerapp.repositories.FoodRepository;
import com.example.weighttrackerapp.repositories.GoalRepository;
import com.example.weighttrackerapp.repositories.UserRepository;
import com.example.weighttrackerapp.repositories.WeightRepository;
import com.example.weighttrackerapp.utils.DateUtils;
import com.example.weighttrackerapp.utils.HealthCalculator;

import java.util.Date;
import java.util.List;

public class DashboardViewModel extends AndroidViewModel {

    private final WeightRepository weightRepository;
    private final GoalRepository goalRepository;
    private final FoodRepository foodRepository;
    private final UserRepository userRepository;

    // Data Streams
    private final LiveData<WeightEntry> latestWeight;
    private final LiveData<List<WeightEntry>> weightHistory;
    private final LiveData<Goal> activeGoal;
    private final LiveData<UserProfile> userProfile;

    // Calculated Targets (The "Smart" part)
    private final MediatorLiveData<HealthCalculator.MacroNutrients> dailyTargets = new MediatorLiveData<>();

    // Nutrition Actuals (What user ate today)
    private final LiveData<Integer> todayCalories;
    private final LiveData<Double> todayProtein;
    private final LiveData<Double> todayCarbs;
    private final LiveData<Double> todayFat;

    // Water Data
    private final MutableLiveData<Integer> waterIntake = new MutableLiveData<>(0);
    private final SharedPreferences prefs;

    public DashboardViewModel(@NonNull Application application) {
        super(application);

        weightRepository = new WeightRepository(application);
        goalRepository = new GoalRepository(application);
        foodRepository = new FoodRepository(application);
        userRepository = new UserRepository(application);

        prefs = application.getSharedPreferences("daily_tracker", Context.MODE_PRIVATE);
        loadTodayWater();

        // 1. Load Data Sources
        latestWeight = weightRepository.getLatestWeightEntry();
        weightHistory = weightRepository.getAllWeightEntries();
        userProfile = userRepository.getUserProfile();

        // Get Active Goal (Safe transformation)
        activeGoal = Transformations.map(goalRepository.getActiveGoals(), list ->
                (list != null && !list.isEmpty()) ? list.get(0) : null
        );

        // 2. Setup Smart Targets Calculation
        // If Profile, Weight, or Goal changes -> Recalculate needs
        dailyTargets.addSource(userProfile, profile -> recalculateTargets());
        dailyTargets.addSource(latestWeight, weight -> recalculateTargets());
        dailyTargets.addSource(activeGoal, goal -> recalculateTargets());

        // 3. Nutrition Actuals (Today)
        Date today = new Date();
        todayCalories = foodRepository.getTotalCaloriesForDate(today);
        todayProtein = foodRepository.getTotalProteinForDate(today);
        todayCarbs = foodRepository.getTotalCarbsForDate(today);
        todayFat = foodRepository.getTotalFatForDate(today);
    }

    private void recalculateTargets() {
        UserProfile profile = userProfile.getValue();
        WeightEntry weight = latestWeight.getValue();
        Goal goal = activeGoal.getValue();

        // We need at least Profile and Weight to calculate anything
        if (profile != null && weight != null) {

            double currentW = weight.getWeight();

            // If no goal exists, calculate for Maintenance (Target = Current, Date = Now)
            double targetW = (goal != null) ? goal.getTargetWeight() : currentW;
            long targetD = (goal != null) ? goal.getTargetDate() : System.currentTimeMillis();

            HealthCalculator.MacroNutrients result = HealthCalculator.calculateNeeds(
                    profile,
                    currentW,
                    targetW,
                    targetD
            );

            dailyTargets.setValue(result);
        }
    }

    // --- Water Logic ---
    private void loadTodayWater() {
        String key = "water_" + DateUtils.formatDate(System.currentTimeMillis());
        waterIntake.setValue(prefs.getInt(key, 0));
    }

    public void addWater() {
        int current = waterIntake.getValue() != null ? waterIntake.getValue() : 0;
        int newVal = current + 1;
        waterIntake.setValue(newVal);
        String key = "water_" + DateUtils.formatDate(System.currentTimeMillis());
        prefs.edit().putInt(key, newVal).apply();
    }

    // --- Getters ---
    public LiveData<WeightEntry> getLatestWeight() { return latestWeight; }
    public LiveData<List<WeightEntry>> getWeightHistory() { return weightHistory; }
    public LiveData<UserProfile> getUserProfile() { return userProfile; }
    public LiveData<Goal> getActiveGoal() { return activeGoal; }

    public LiveData<HealthCalculator.MacroNutrients> getDailyTargets() { return dailyTargets; }

    public LiveData<Integer> getTodayCalories() { return todayCalories; }
    public LiveData<Double> getTodayProtein() { return todayProtein; }
    public LiveData<Double> getTodayCarbs() { return todayCarbs; }
    public LiveData<Double> getTodayFat() { return todayFat; }
    public LiveData<Integer> getWaterIntake() { return waterIntake; }
}