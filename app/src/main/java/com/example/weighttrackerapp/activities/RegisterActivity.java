package com.example.weighttrackerapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.weighttrackerapp.R;
import com.example.weighttrackerapp.models.UserProfile;
import com.example.weighttrackerapp.utils.SecurityUtils;
import com.example.weighttrackerapp.utils.SessionManager;
import com.example.weighttrackerapp.viewmodels.AuthViewModel;

public class RegisterActivity extends AppCompatActivity {

    private AuthViewModel viewModel;
    private SessionManager sessionManager;

    // UI Components
    private EditText etName, etEmail, etPassword, etAge, etHeight, etWeight;
    private Spinner spGender, spActivityLevel;
    private RadioGroup rgQ1, rgQ2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sessionManager = new SessionManager(this);
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        initializeViews();
        setupSpinners();

        Button btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(v -> performRegistration());
    }

    private void initializeViews() {
        etName = findViewById(R.id.et_reg_name);
        etEmail = findViewById(R.id.et_reg_email);
        etPassword = findViewById(R.id.et_reg_password);
        etAge = findViewById(R.id.et_reg_age);
        etHeight = findViewById(R.id.et_reg_height);
        etWeight = findViewById(R.id.et_reg_weight); // New
        spGender = findViewById(R.id.sp_reg_gender);
        spActivityLevel = findViewById(R.id.sp_reg_activity);

        // Quiz
        rgQ1 = findViewById(R.id.rg_q1);
        rgQ2 = findViewById(R.id.rg_q2);
    }

    private void setupSpinners() {
        // Gender
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{UserProfile.GENDER_MALE, UserProfile.GENDER_FEMALE});
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(genderAdapter);

        // Activity Level
        ArrayAdapter<String> activityAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{
                        UserProfile.LEVEL_SEDENTARY,
                        UserProfile.LEVEL_LIGHT,
                        UserProfile.LEVEL_MODERATE,
                        UserProfile.LEVEL_ACTIVE
                });
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spActivityLevel.setAdapter(activityAdapter);
    }

    private void performRegistration() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String heightStr = etHeight.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() ||
                ageStr.isEmpty() || heightStr.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check Quiz
        if (rgQ1.getCheckedRadioButtonId() == -1 || rgQ2.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please answer the metabolism questions", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int age = Integer.parseInt(ageStr);
            double height = Double.parseDouble(heightStr);
            double weight = Double.parseDouble(weightStr);
            String gender = spGender.getSelectedItem().toString();
            String activity = spActivityLevel.getSelectedItem().toString();

            // Calculate Metabolism
            String metabolism = calculateMetabolism();

            // Hash Password
            String hashedPassword = SecurityUtils.hashString(password);

            // Create User Object with NEW fields
            UserProfile newUser = new UserProfile(
                    name, email, hashedPassword, age, gender, height, weight, activity, metabolism
            );

            // Save to DB
            viewModel.register(newUser).observe(this, userId -> {
                if (userId != null && userId > 0) {
                    Toast.makeText(this, "Welcome! Metabolism: " + metabolism, Toast.LENGTH_LONG).show();

                    // FIXED: Now we have the userId for the session!
                    sessionManager.createLoginSession(userId, name);

                    startActivity(new Intent(this, MainActivity.class));
                    finishAffinity();
                } else {
                    Toast.makeText(this, "Email already exists!", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
        }
    }

    // Simple logic to determine type
    private String calculateMetabolism() {
        int score = 0;

        // Question 1: Weight Gain
        int id1 = rgQ1.getCheckedRadioButtonId();
        if (id1 == R.id.rb_q1_easy) score += 1;      // Slow
        else if (id1 == R.id.rb_q1_hard) score += 3; // Fast
        else score += 2;                             // Average

        // Question 2: Energy after food
        int id2 = rgQ2.getCheckedRadioButtonId();
        if (id2 == R.id.rb_q2_sleepy) score += 1;    // Slow/Insulin resistant
        else score += 2;                             // Fast/Normal

        // Result
        if (score <= 2) return "Slow (Endomorph)";
        if (score >= 4) return "Fast (Ectomorph)";
        return "Average (Mesomorph)";
    }
}