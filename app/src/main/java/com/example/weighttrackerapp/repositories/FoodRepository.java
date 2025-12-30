package com.example.weighttrackerapp.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.weighttrackerapp.database.AppDatabase;
import com.example.weighttrackerapp.database.FoodEntryDao;
import com.example.weighttrackerapp.database.FoodItemDao;
import com.example.weighttrackerapp.models.FoodEntry;
import com.example.weighttrackerapp.models.FoodItem;
import com.example.weighttrackerapp.utils.DateUtils;
import com.example.weighttrackerapp.utils.SessionManager;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FoodRepository {

    private final FoodEntryDao foodEntryDao;
    private final FoodItemDao foodItemDao;
    private final ExecutorService executorService;
    private final int currentUserId; // Store User ID

    public FoodRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        foodEntryDao = db.foodEntryDao();
        foodItemDao = db.foodItemDao();
        executorService = Executors.newFixedThreadPool(4);

        // Get User ID
        SessionManager session = new SessionManager(application);
        currentUserId = session.getUserId();
    }

    // --- Write Ops (User Specific) ---
    public void insert(FoodEntry entry) {
        entry.setUserId(currentUserId); // Link to user
        executorService.execute(() -> foodEntryDao.insert(entry));
    }

    public void delete(FoodEntry entry) {
        executorService.execute(() -> foodEntryDao.delete(entry));
    }

    // --- Read Ops (User Specific) ---
    public LiveData<List<FoodEntry>> getFoodEntriesForDate(Date date) {
        long start = DateUtils.getStartOfDay(date);
        long end = DateUtils.getEndOfDay(date);
        return foodEntryDao.getFoodEntriesForDay(currentUserId, start, end);
    }

    public LiveData<Integer> getTotalCaloriesForDate(Date date) {
        return foodEntryDao.getTotalCaloriesForDay(
                currentUserId,
                DateUtils.getStartOfDay(date),
                DateUtils.getEndOfDay(date)
        );
    }

    public LiveData<Double> getTotalProteinForDate(Date date) {
        return foodEntryDao.getTotalProteinForDay(
                currentUserId,
                DateUtils.getStartOfDay(date),
                DateUtils.getEndOfDay(date)
        );
    }

    public LiveData<Double> getTotalCarbsForDate(Date date) {
        return foodEntryDao.getTotalCarbsForDay(
                currentUserId,
                DateUtils.getStartOfDay(date),
                DateUtils.getEndOfDay(date)
        );
    }

    public LiveData<Double> getTotalFatForDate(Date date) {
        return foodEntryDao.getTotalFatForDay(
                currentUserId,
                DateUtils.getStartOfDay(date),
                DateUtils.getEndOfDay(date)
        );
    }

    // --- Global Operations (No User ID needed) ---
    public LiveData<List<FoodItem>> searchFoodItems(String query) {
        return foodItemDao.searchFood(query);
    }

    public void addFoodItem(FoodItem item) {
        executorService.execute(() -> foodItemDao.insert(item));
    }

    public FoodItem getFoodItemByNameSync(String name) {
        return foodItemDao.getFoodItemByNameSync(name);
    }

    public void update(FoodEntry entry) {
        entry.setUserId(currentUserId);
        executorService.execute(() -> foodEntryDao.update(entry));
    }
}