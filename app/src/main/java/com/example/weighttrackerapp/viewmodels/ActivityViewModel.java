package com.example.weighttrackerapp.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.weighttrackerapp.models.ActivityEntry;
import com.example.weighttrackerapp.repositories.ActivityRepository;

import java.util.Date;
import java.util.List;

public class ActivityViewModel extends AndroidViewModel {

    private final ActivityRepository repository;

    // State: The currently selected date (defaults to Today)
    private final MutableLiveData<Date> selectedDate = new MutableLiveData<>(new Date());

    // LiveData that updates automatically when selectedDate changes
    private final LiveData<List<ActivityEntry>> dailyActivities;
    private final LiveData<Integer> dailyCaloriesBurned;
    private final LiveData<Integer> dailyDuration;

    public ActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new ActivityRepository(application);

        // Setup Transformations
        dailyActivities = Transformations.switchMap(selectedDate, repository::getActivitiesForDate);
        dailyCaloriesBurned = Transformations.switchMap(selectedDate, repository::getTotalCaloriesBurnedForDate);
        dailyDuration = Transformations.switchMap(selectedDate, repository::getTotalDurationForDate);
    }

    // --- Getters for UI ---

    public LiveData<List<ActivityEntry>> getDailyActivities() {
        return dailyActivities;
    }

    public LiveData<Integer> getDailyCaloriesBurned() {
        return dailyCaloriesBurned;
    }

    public LiveData<Integer> getDailyDuration() {
        return dailyDuration;
    }

    // --- Actions ---

    public void setSelectedDate(Date date) {
        selectedDate.setValue(date);
    }

    public Date getSelectedDateValue() {
        return selectedDate.getValue();
    }

    public void addActivity(ActivityEntry entry) {
        repository.insert(entry);
    }

    public void updateActivity(ActivityEntry entry) {
        repository.update(entry);
    }

    public void deleteActivity(ActivityEntry entry) {
        repository.delete(entry);
    }
}