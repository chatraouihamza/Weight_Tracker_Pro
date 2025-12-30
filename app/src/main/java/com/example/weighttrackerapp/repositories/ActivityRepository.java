package com.example.weighttrackerapp.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.weighttrackerapp.database.ActivityEntryDao;
import com.example.weighttrackerapp.database.AppDatabase;
import com.example.weighttrackerapp.models.ActivityEntry;
import com.example.weighttrackerapp.utils.DateUtils;
import com.example.weighttrackerapp.utils.SessionManager;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ActivityRepository {

    private final ActivityEntryDao activityEntryDao;
    private final ExecutorService executorService;
    private final int currentUserId;

    public ActivityRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        activityEntryDao = db.activityEntryDao();
        executorService = Executors.newFixedThreadPool(4);

        SessionManager session = new SessionManager(application);
        currentUserId = session.getUserId();
    }

    // --- Write Operations ---

    public void insert(ActivityEntry entry) {
        entry.setUserId(currentUserId);
        executorService.execute(() -> activityEntryDao.insert(entry));
    }

    public void update(ActivityEntry entry) {
        entry.setUserId(currentUserId);
        executorService.execute(() -> activityEntryDao.update(entry));
    }

    public void delete(ActivityEntry entry) {
        executorService.execute(() -> activityEntryDao.delete(entry));
    }

    // --- Read Operations ---

    public LiveData<List<ActivityEntry>> getAllActivities() {
        return activityEntryDao.getAllActivityEntries(currentUserId);
    }

    public LiveData<List<ActivityEntry>> getActivitiesForDate(Date date) {
        long start = DateUtils.getStartOfDay(date);
        long end = DateUtils.getEndOfDay(date);
        return activityEntryDao.getActivityEntriesForDay(currentUserId, start, end);
    }

    public LiveData<Integer> getTotalCaloriesBurnedForDate(Date date) {
        long start = DateUtils.getStartOfDay(date);
        long end = DateUtils.getEndOfDay(date);
        return activityEntryDao.getTotalCaloriesBurnedForDay(currentUserId, start, end);
    }

    public LiveData<Integer> getTotalDurationForDate(Date date) {
        long start = DateUtils.getStartOfDay(date);
        long end = DateUtils.getEndOfDay(date);
        return activityEntryDao.getTotalDurationForDay(currentUserId, start, end);
    }
}