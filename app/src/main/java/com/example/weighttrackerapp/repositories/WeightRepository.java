package com.example.weighttrackerapp.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.weighttrackerapp.database.AppDatabase;
import com.example.weighttrackerapp.database.WeightEntryDao;
import com.example.weighttrackerapp.models.WeightEntry;
import com.example.weighttrackerapp.utils.SessionManager;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeightRepository {

    private final WeightEntryDao weightEntryDao;
    private final int currentUserId;
    private final ExecutorService executorService;

    public WeightRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        weightEntryDao = db.weightEntryDao();
        executorService = Executors.newFixedThreadPool(4);
        SessionManager session = new SessionManager(application);
        currentUserId = session.getUserId();
    }

    // --- READ Operations ---

    public LiveData<List<WeightEntry>> getAllWeightEntries() {
        return weightEntryDao.getAllWeightEntries(currentUserId);
    }

    public LiveData<WeightEntry> getLatestWeightEntry() {
        return weightEntryDao.getLatestWeightEntry(currentUserId);
    }

    public LiveData<Double> getAverageWeight() {
        return weightEntryDao.getAverageWeight(currentUserId);
    }

    // FIX: Removed 'int currentUserId' parameter. Use 'this.currentUserId' instead.
    public LiveData<List<WeightEntry>> getWeightEntriesBetweenDates(long start, long end) {
        return weightEntryDao.getWeightEntriesBetweenDates(currentUserId, start, end);
    }

    // --- WRITE Operations ---

    public void insert(WeightEntry entry) {
        // FIX: IMPORTANT! Link the entry to the current user before saving
        entry.setUserId(currentUserId);
        executorService.execute(() -> weightEntryDao.insert(entry));
    }

    public void update(WeightEntry entry) {
        // Ensure user ID is correct
        entry.setUserId(currentUserId);
        // FIX: Changed 'insert' to 'update'
        executorService.execute(() -> weightEntryDao.update(entry));
    }

    public void delete(WeightEntry entry) {
        executorService.execute(() -> weightEntryDao.delete(entry));
    }


}