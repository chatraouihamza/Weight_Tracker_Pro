package com.example.weighttrackerapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.weighttrackerapp.R;
import com.example.weighttrackerapp.viewmodels.DashboardViewModel;
import com.example.weighttrackerapp.utils.HealthCalculator;
import com.example.weighttrackerapp.utils.FormatUtils;

/**
 * Dashboard Fragment - Main overview of user's health metrics.
 */
public class DashboardFragment extends Fragment {
    
    private DashboardViewModel viewModel;
    private TextView tvCurrentWeight, tvBMI, tvBMICategory, tvGoalProgress;
    private TextView tvLastUpdate, tvWeightChange, tvCaloriestoday;
    private ProgressBar pbGoalProgress;
    private LinearLayout llQuickStats;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initializeViews(view);
        setupViewModel();
        observeData();
    }
    
    private void initializeViews(View view) {
        tvCurrentWeight = view.findViewById(R.id.tv_current_weight);
        tvBMI = view.findViewById(R.id.tv_bmi);
        tvBMICategory = view.findViewById(R.id.tv_bmi_category);
        tvGoalProgress = view.findViewById(R.id.tv_goal_progress);
        tvLastUpdate = view.findViewById(R.id.tv_last_update);
        tvWeightChange = view.findViewById(R.id.tv_weight_change);
        tvCaloriestoday = view.findViewById(R.id.tv_calories_today);
        pbGoalProgress = view.findViewById(R.id.pb_goal_progress);
        llQuickStats = view.findViewById(R.id.ll_quick_stats);
    }
    
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
    }
    
    private void observeData() {
        // Observe latest weight entry
        viewModel.getLatestWeightEntry().observe(getViewLifecycleOwner(), weightEntry -> {
            if (weightEntry != null) {
                tvCurrentWeight.setText(FormatUtils.formatWeight(weightEntry.getWeight()) + " " + 
                                       viewModel.getUnitPreference());
                tvLastUpdate.setText("Updated: " + FormatUtils.formatDateTime(weightEntry.getDate()));
                
                // Calculate and display BMI
                double bmi = HealthCalculator.calculateBMI(weightEntry.getWeight(), 
                                                          viewModel.getUserHeight());
                tvBMI.setText(FormatUtils.formatBMI(bmi));
                tvBMICategory.setText(HealthCalculator.getBMICategory(bmi));
            }
        });
        
        // Observe weight change
        viewModel.getWeightChange().observe(getViewLifecycleOwner(), change -> {
            if (change != null) {
                String changeText = (change >= 0 ? "+" : "") + FormatUtils.formatWeight(change);
                tvWeightChange.setText(changeText);
                tvWeightChange.setTextColor(change < 0 ? 
                    getResources().getColor(R.color.success_green) : 
                    getResources().getColor(R.color.warning_orange));
            }
        });
        
        // Observe goal progress
        viewModel.getActiveGoal().observe(getViewLifecycleOwner(), goal -> {
            if (goal != null) {
                pbGoalProgress.setProgress(goal.getProgressPercentage());
                tvGoalProgress.setText(goal.getProgressPercentage() + "% - " + goal.getTitle());
            }
        });
        
        // Observe daily calories
        viewModel.getDailyCalories().observe(getViewLifecycleOwner(), calories -> {
            if (calories != null) {
                tvCaloriestoday.setText(calories + " kcal");
            }
        });
    }
}
