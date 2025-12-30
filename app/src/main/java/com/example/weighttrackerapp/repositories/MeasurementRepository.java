package com.example.weighttrackerapp.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.weighttrackerapp.database.AppDatabase;
import com.example.weighttrackerapp.database.MeasurementDao;
import com.example.weighttrackerapp.models.Measurement;
import com.example.weighttrackerapp.utils.SessionManager;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MeasurementRepository {

    private final MeasurementDao measurementDao;
    private final LiveData<List<Measurement>> allMeasurements;
    private final ExecutorService executorService;
    private final int currentUserId;

    public MeasurementRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        measurementDao = db.measurementDao();
        executorService = Executors.newFixedThreadPool(4);

        SessionManager session = new SessionManager(application);
        currentUserId = session.getUserId();

        // Initialize LiveData with User ID
        allMeasurements = measurementDao.getAllMeasurements(currentUserId);
    }

    // --- Read Operations ---

    public LiveData<List<Measurement>> getAllMeasurements() {
        return allMeasurements;
    }

    public LiveData<Measurement> getLatestMeasurement() {
        return measurementDao.getLatestMeasurement(currentUserId);
    }

    public LiveData<List<Measurement>> getMeasurementsBetweenDates(long start, long end) {
        return measurementDao.getMeasurementsBetweenDates(currentUserId, start, end);
    }

    // --- Write Operations ---

    public void insert(Measurement measurement) {
        measurement.setUserId(currentUserId);
        executorService.execute(() -> measurementDao.insert(measurement));
    }

    public void update(Measurement measurement) {
        measurement.setUserId(currentUserId);
        executorService.execute(() -> measurementDao.update(measurement));
    }

    public void delete(Measurement measurement) {
        executorService.execute(() -> measurementDao.delete(measurement));
    }
}