package com.example.weighttrackerapp.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.weighttrackerapp.models.UserProfile;
import com.example.weighttrackerapp.repositories.AuthRepository;

public class AuthViewModel extends AndroidViewModel {
    private final AuthRepository repository;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        repository = new AuthRepository(application);
    }

    public LiveData<Integer> register(UserProfile user) {
        return repository.register(user);
    }

    public LiveData<UserProfile> login(String email, String password) {
        return repository.login(email, password);
    }
}