package com.example.weighttrackerapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.weighttrackerapp.R;
import com.example.weighttrackerapp.fragments.ProfileFragment;
import com.example.weighttrackerapp.fragments.DashboardFragment;
import com.example.weighttrackerapp.fragments.FoodJournalFragment;
import com.example.weighttrackerapp.fragments.GoalsFragment;
import com.example.weighttrackerapp.fragments.WeightTrackingFragment;
import com.example.weighttrackerapp.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

/**
 * Main activity of Weight Tracker Pro application.
 * Manages bottom navigation and fragment switching.
 */
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Handle Splash Screen (API 31+)
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        // 1. SECURITY CHECK: Ensure user is logged in
        sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn()) {
            redirectToLogin();
            return; // Stop execution
        }

        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // 2. MODERN LISTENER: Use NavigationBarView.OnItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);

        // Load default fragment (Dashboard)
        if (savedInstanceState == null) {
            loadFragment(new DashboardFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_dashboard);
        }
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        int itemId = item.getItemId();

        if (itemId == R.id.nav_dashboard) {
            fragment = new DashboardFragment();
        } else if (itemId == R.id.nav_weight) {
            fragment = new WeightTrackingFragment();
        } else if (itemId == R.id.nav_goals) {
            fragment = new GoalsFragment();
        } else if (itemId == R.id.nav_food) {
            fragment = new FoodJournalFragment();
        } else if (itemId == R.id.nav_profile) {
            fragment = new ProfileFragment();
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            // IMPORTANT: Ensure R.id.nav_host_fragment matches your activity_main.xml ID
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        // Clear back stack so user can't go back to Main
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}