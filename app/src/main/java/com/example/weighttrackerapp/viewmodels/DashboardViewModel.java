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

import com.example.weighttrackerapp.models.FoodEntry;
import com.example.weighttrackerapp.models.Goal;
import com.example.weighttrackerapp.models.MealGroup;
import com.example.weighttrackerapp.models.UserProfile;
import com.example.weighttrackerapp.models.WeightEntry;
import com.example.weighttrackerapp.repositories.FoodRepository;
import com.example.weighttrackerapp.repositories.GoalRepository;
import com.example.weighttrackerapp.repositories.UserRepository;
import com.example.weighttrackerapp.repositories.WeightRepository;
import com.example.weighttrackerapp.utils.DateUtils;
import com.example.weighttrackerapp.utils.HealthCalculator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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



    public LiveData<List<MealGroup>> getTodayMealGroups() {
        // Fetch raw list for today -> Convert to Grouped List
        return Transformations.map(foodRepository.getFoodEntriesForDate(new Date()), rawList -> {
            Map<String, MealGroup> groups = new HashMap<>();

            if (rawList != null) {
                for (FoodEntry entry : rawList) {
                    String type = entry.getMealType();
                    // If group doesn't exist, create it
                    if (!groups.containsKey(type)) {
                        groups.put(type, new MealGroup(type));
                    }
                    // Add entry to group
                    groups.get(type).addEntry(entry);
                }
            }
            // Convert Map values to List
            return new ArrayList<>(groups.values());
        });
    }

    public void updateFoodEntryByGrams(FoodEntry entry, double newGrams) {
        // Run in background because we need to query the FoodItem table
        java.util.concurrent.Executors.newSingleThreadExecutor().execute(() -> {

            // 1. Find the base definition (per 100g)
            com.example.weighttrackerapp.models.FoodItem baseItem =
                    foodRepository.getFoodItemByNameSync(entry.getFoodName());

            if (baseItem != null) {
                // 2. Recalculate Macros
                double ratio = newGrams / 100.0;

                entry.setCalories((int) (baseItem.getCalories() * ratio));
                entry.setProtein(baseItem.getProtein() * ratio);
                entry.setCarbs(baseItem.getCarbs() * ratio);
                entry.setFat(baseItem.getFat() * ratio);

                // 3. Update DB
                foodRepository.update(entry);
            } else {
                // Fallback: If we can't find the base item (rare), maybe just leave it
                // or you could implement simple scaling if you had stored previous grams.
                // For now, we assume the name matches.
            }
        });
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

    public LiveData<List<FoodEntry>> getTodayFoodEntries() {
        return foodRepository.getFoodEntriesForDate(new Date());
    }

    // Actions
    public void updateFoodEntry(FoodEntry entry) {
        foodRepository.update(entry);
    }

    public void deleteFoodEntry(FoodEntry entry) {
        foodRepository.delete(entry);
    }

    public void addWater() {
        int current = waterIntake.getValue() != null ? waterIntake.getValue() : 0;
        updateWater(current + 1);
    }

    public void removeWater() {
        int current = waterIntake.getValue() != null ? waterIntake.getValue() : 0;
        if (current > 0) {
            updateWater(current - 1);
        }
    }

    private void updateWater(int quantity) {
        waterIntake.setValue(quantity);
        String key = "water_" + DateUtils.formatDate(System.currentTimeMillis());
        prefs.edit().putInt(key, quantity).apply();
    }
}