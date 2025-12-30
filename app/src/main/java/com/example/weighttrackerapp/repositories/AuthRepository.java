package com.example.weighttrackerapp.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weighttrackerapp.database.AppDatabase;
import com.example.weighttrackerapp.database.UserProfileDao;
import com.example.weighttrackerapp.database.WeightEntryDao;
import com.example.weighttrackerapp.models.UserProfile;
import com.example.weighttrackerapp.models.WeightEntry;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuthRepository {
    private final UserProfileDao userDao;
    private final WeightEntryDao weightDao;
    private final ExecutorService executor;

    public AuthRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        userDao = db.userProfileDao();
        weightDao = db.weightEntryDao(); // Initialize it
        executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<Integer> register(UserProfile user) {
        MutableLiveData<Integer> result = new MutableLiveData<>();
        executor.execute(() -> {
            // 1. Check if user already exists
            if (userDao.checkEmailExists(user.getEmail()) > 0) {
                result.postValue(-1);
            } else {
                // 2. Insert User and get the new ID
                long newId = userDao.insert(user);

                if (newId > 0) {
                    // 3. AUTOMATICALLY ADD FIRST WEIGHT ENTRY
                    WeightEntry initialEntry = new WeightEntry(
                            user.getInitialWeight(), // Use the weight from registration
                            System.currentTimeMillis()
                    );
                    initialEntry.setUserId((int) newId); // Link to new user

                    weightDao.insert(initialEntry); // Save to history
                }

                result.postValue((int) newId);
            }
        });
        return result;
    }

    // ... login method remains the same ...
    public LiveData<UserProfile> login(String email, String password) {
        MutableLiveData<UserProfile> result = new MutableLiveData<>();
        executor.execute(() -> {
            UserProfile user = userDao.login(email, password);
            result.postValue(user);
        });
        return result;
    }
}