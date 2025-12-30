package com.example.weighttrackerapp.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.weighttrackerapp.models.FoodItem;
import com.example.weighttrackerapp.models.FoodEntry;
import com.example.weighttrackerapp.repositories.FoodRepository;

import java.util.Date;
import java.util.List;

public class FoodJournalViewModel extends AndroidViewModel {

    private final FoodRepository repository;

    // We use a MutableLiveData for the "Selected Date" (default: Today)
    private final MutableLiveData<Date> selectedDate = new MutableLiveData<>(new Date());

    // These LiveData depend on the selectedDate. When date changes, these update automatically.
    private final LiveData<List<FoodEntry>> dailyEntries;
    private final LiveData<Integer> dailyCalories;
    private final LiveData<Double> dailyProtein;
    private final LiveData<Double> dailyCarbs;
    private final LiveData<Double> dailyFat;

    public FoodJournalViewModel(@NonNull Application application) {
        super(application);
        repository = new FoodRepository(application);

        // Transformation map: If selectedDate changes -> call repository with new date
        dailyEntries = Transformations.switchMap(selectedDate, repository::getFoodEntriesForDate);
        dailyCalories = Transformations.switchMap(selectedDate, repository::getTotalCaloriesForDate);
        dailyProtein = Transformations.switchMap(selectedDate, repository::getTotalProteinForDate);
        dailyCarbs = Transformations.switchMap(selectedDate, repository::getTotalCarbsForDate);
        dailyFat = Transformations.switchMap(selectedDate, repository::getTotalFatForDate);
    }

    // --- Getters ---
    public LiveData<List<FoodEntry>> getDailyEntries() { return dailyEntries; }
    public LiveData<Integer> getDailyCalories() { return dailyCalories; }
    public LiveData<Double> getDailyProtein() { return dailyProtein; }
    public LiveData<Double> getDailyCarbs() { return dailyCarbs; }
    public LiveData<Double> getDailyFat() { return dailyFat; }

    // --- Actions ---
    public void setSelectedDate(Date date) {
        selectedDate.setValue(date);
    }

    public void insertFoodEntry(FoodEntry entry) {
        repository.insert(entry);
    }

    public void deleteFoodEntry(FoodEntry entry) {
        repository.delete(entry);
    }

    public LiveData<List<FoodItem>> searchFood(String query) {
        return repository.searchFoodItems(query);
    }

    public void createCustomFood(String name, int cals, double pro, double carb, double fat) {
        FoodItem newItem = new FoodItem(name, cals, pro, carb, fat);
        repository.addFoodItem(newItem);
    }
}