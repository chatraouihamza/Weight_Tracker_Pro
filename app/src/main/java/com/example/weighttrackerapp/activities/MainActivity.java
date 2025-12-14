package com.example.weighttrackerapp.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.weighttrackerapp.R;
import com.example.weighttrackerapp.fragments.DashboardFragment;
import com.example.weighttrackerapp.fragments.WeightTrackingFragment;
import com.example.weighttrackerapp.fragments.GoalsFragment;
import com.example.weighttrackerapp.fragments.FoodJournalFragment;
import com.example.weighttrackerapp.fragments.AnalyticsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Main activity of Weight Tracker Pro application.
 * Manages bottom navigation and fragment switching.
 */
public class MainActivity extends AppCompatActivity {
    
    private BottomNavigationView bottomNavigationView;
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_main);
        
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
        
        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new DashboardFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_dashboard);
        }
    }
    
    private boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        
        if (item.getItemId() == R.id.nav_dashboard) {
            fragment = new DashboardFragment();
        } else if (item.getItemId() == R.id.nav_weight) {
            fragment = new WeightTrackingFragment();
        } else if (item.getItemId() == R.id.nav_goals) {
            fragment = new GoalsFragment();
        } else if (item.getItemId() == R.id.nav_food) {
            fragment = new FoodJournalFragment();
        } else if (item.getItemId() == R.id.nav_analytics) {
            fragment = new AnalyticsFragment();
        }
        
        return loadFragment(fragment);
    }
    
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
