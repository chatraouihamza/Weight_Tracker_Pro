package com.example.weighttrackerapp.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.weighttrackerapp.models.Measurement;
import com.example.weighttrackerapp.repositories.MeasurementRepository;

import java.util.List;

public class MeasurementViewModel extends AndroidViewModel {

    private final MeasurementRepository repository;
    private final LiveData<List<Measurement>> allMeasurements;

    public MeasurementViewModel(@NonNull Application application) {
        super(application);
        repository = new MeasurementRepository(application);
        allMeasurements = repository.getAllMeasurements();
    }

    public LiveData<List<Measurement>> getAllMeasurements() {
        return allMeasurements;
    }

    public LiveData<Measurement> getLatestMeasurement() {
        return repository.getLatestMeasurement();
    }

    public void insert(Measurement measurement) {
        repository.insert(measurement);
    }

    public void update(Measurement measurement) {
        repository.update(measurement);
    }

    public void delete(Measurement measurement) {
        repository.delete(measurement);
    }
}