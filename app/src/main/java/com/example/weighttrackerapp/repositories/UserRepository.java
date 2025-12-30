package com.example.weighttrackerapp.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.weighttrackerapp.database.AppDatabase;
import com.example.weighttrackerapp.database.UserProfileDao;
import com.example.weighttrackerapp.models.UserProfile;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {

    private final UserProfileDao userProfileDao;
    private final LiveData<UserProfile> userProfile;
    private final ExecutorService executorService;

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        userProfileDao = db.userProfileDao();
        userProfile = userProfileDao.getUserProfile();
        executorService = Executors.newFixedThreadPool(2);
    }

    public LiveData<UserProfile> getUserProfile() {
        return userProfile;
    }

    public LiveData<Integer> hasProfile() {
        return userProfileDao.hasProfile();
    }

    public void saveUserProfile(UserProfile profile) {
        // Ensure ID is always 1
        profile.setId(1);
        executorService.execute(() -> userProfileDao.insert(profile));
    }

    public void updateUserProfile(UserProfile profile) {
        profile.setId(1);
        executorService.execute(() -> userProfileDao.update(profile));
    }
}