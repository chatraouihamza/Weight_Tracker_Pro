package com.example.weighttrackerapp.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.weighttrackerapp.models.UserProfile;
import com.example.weighttrackerapp.repositories.UserRepository;

public class UserProfileViewModel extends AndroidViewModel {

    private final UserRepository repository;
    private final LiveData<UserProfile> userProfile;

    public UserProfileViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        userProfile = repository.getUserProfile();
    }

    public LiveData<UserProfile> getUserProfile() {
        return userProfile;
    }

    public LiveData<Integer> hasProfile() {
        return repository.hasProfile();
    }

    public void saveProfile(UserProfile profile) {
        profile.setId(1); // Enforce Singleton ID
        repository.saveUserProfile(profile);
    }

    // Ensure this method exists for the Edit Profile feature
    public void updateProfile(UserProfile profile) {
        profile.setId(1); // Enforce Singleton ID
        repository.updateUserProfile(profile);
    }
}