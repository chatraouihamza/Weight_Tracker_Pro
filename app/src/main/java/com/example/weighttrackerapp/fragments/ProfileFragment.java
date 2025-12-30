package com.example.weighttrackerapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.content.ContextCompat;
import androidx.activity.result.contract.ActivityResultContracts;
import com.example.weighttrackerapp.utils.NotificationScheduler;
import com.google.android.material.switchmaterial.SwitchMaterial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.weighttrackerapp.R;
import com.example.weighttrackerapp.activities.LoginActivity;
import com.example.weighttrackerapp.models.UserProfile;
import com.example.weighttrackerapp.utils.SessionManager;
import com.example.weighttrackerapp.viewmodels.UserProfileViewModel;

public class ProfileFragment extends Fragment {

    // CHANGED: Use UserProfileViewModel
    private UserProfileViewModel viewModel;
    private UserProfile currentUser;

    // UI
    private TextView tvProfileName;
    private EditText etName, etAge, etHeight;
    private Spinner spActivityLevel;
    private Button btnSave, btnLogout;

    // Notifications
    private SwitchMaterial switchNotifications;
    private final androidx.activity.result.ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    NotificationScheduler.scheduleDailyReminders(requireContext());
                    Toast.makeText(getContext(), "Reminders Enabled", Toast.LENGTH_SHORT).show();
                } else {
                    switchNotifications.setChecked(false);
                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
        setupSpinner();
        setupViewModel();

        btnSave.setOnClickListener(v -> saveProfile());
        btnLogout.setOnClickListener(v -> logout());

        // Load initial state (from UserProfile)
        viewModel.getUserProfile().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                // ... existing population ...
                switchNotifications.setChecked(profile.isNotificationsEnabled());
            }
        });

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (currentUser != null) {
                currentUser.setNotificationsEnabled(isChecked);
                viewModel.updateProfile(currentUser); // Save pref to DB

                if (isChecked) {
                    checkPermissionAndSchedule();
                } else {
                    NotificationScheduler.cancelAll(requireContext());
                }
            }
        });
    }

    private void initializeViews(View view) {
        tvProfileName = view.findViewById(R.id.tv_profile_name);
        etName = view.findViewById(R.id.et_name);
        etAge = view.findViewById(R.id.et_age);
        etHeight = view.findViewById(R.id.et_height);
        spActivityLevel = view.findViewById(R.id.sp_activity_level);
        btnSave = view.findViewById(R.id.btn_save_profile);
        btnLogout = view.findViewById(R.id.btn_logout);
        switchNotifications = view.findViewById(R.id.switch_notifications);

    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item,
                new String[]{
                        UserProfile.LEVEL_SEDENTARY,
                        UserProfile.LEVEL_LIGHT,
                        UserProfile.LEVEL_MODERATE,
                        UserProfile.LEVEL_ACTIVE
                });
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spActivityLevel.setAdapter(adapter);
    }

    private void setupViewModel() {
        // CHANGED: Get the existing UserProfileViewModel
        viewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);

        viewModel.getUserProfile().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                currentUser = profile;
                populateFields(profile);
            }
        });
    }

    private void populateFields(UserProfile profile) {
        tvProfileName.setText(profile.getName());
        etName.setText(profile.getName());
        etAge.setText(String.valueOf(profile.getAge()));
        etHeight.setText(String.valueOf(profile.getHeight()));

        // Set Spinner Selection
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spActivityLevel.getAdapter();
        if (adapter != null && profile.getActivityLevel() != null) {
            int position = adapter.getPosition(profile.getActivityLevel());
            if (position >= 0) spActivityLevel.setSelection(position);
        }
    }

    private void saveProfile() {
        if (currentUser == null) return;

        String name = etName.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String heightStr = etHeight.getText().toString().trim();

        if (name.isEmpty() || ageStr.isEmpty() || heightStr.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            currentUser.setName(name);
            currentUser.setAge(Integer.parseInt(ageStr));
            currentUser.setHeight(Double.parseDouble(heightStr));
            currentUser.setActivityLevel(spActivityLevel.getSelectedItem().toString());

            // Note: Password/Email are not updated here for security simplicity in this academic scope

            viewModel.updateProfile(currentUser);
            Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid numbers", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPermissionAndSchedule() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            } else {
                NotificationScheduler.scheduleDailyReminders(requireContext());
            }
        } else {
            // Android < 13 doesn't need runtime permission
            NotificationScheduler.scheduleDailyReminders(requireContext());
        }
    }

    private void logout() {
        SessionManager sessionManager = new SessionManager(requireContext());
        sessionManager.logoutUser();
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}