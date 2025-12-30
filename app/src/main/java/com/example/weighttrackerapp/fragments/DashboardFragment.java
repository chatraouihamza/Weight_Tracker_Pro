package com.example.weighttrackerapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.weighttrackerapp.R;
import com.example.weighttrackerapp.models.Goal;
import com.example.weighttrackerapp.models.UserProfile;
import com.example.weighttrackerapp.models.WeightEntry;
import com.example.weighttrackerapp.utils.HealthCalculator;
import com.example.weighttrackerapp.utils.WeightTargetView;
import com.example.weighttrackerapp.viewmodels.DashboardViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class DashboardFragment extends Fragment {

    private DashboardViewModel viewModel;

    // UI Components - Nutrition
    private TextView tvCalVal, tvProteinVal, tvCarbsVal, tvFatVal;
    private LinearProgressIndicator progCal, progProtein, progCarbs, progFat;

    // UI Components - Water
    private TextView tvWaterCount;
    private FloatingActionButton btnAddWater;

    // UI Components - Goal (Custom View)
    private TextView tvGoalSummary;
    private WeightTargetView weightTargetView;

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
        // --- 1. Nutrition Views ---
        tvCalVal = view.findViewById(R.id.tv_cal_val);
        tvProteinVal = view.findViewById(R.id.tv_protein_val);
        tvCarbsVal = view.findViewById(R.id.tv_carbs_val);
        tvFatVal = view.findViewById(R.id.tv_fat_val);

        progCal = view.findViewById(R.id.progress_calories);
        progProtein = view.findViewById(R.id.progress_protein);
        progCarbs = view.findViewById(R.id.progress_carbs);
        progFat = view.findViewById(R.id.progress_fat);

        // --- 2. Water Views ---
        tvWaterCount = view.findViewById(R.id.tv_water_count);
        btnAddWater = view.findViewById(R.id.btn_add_water);

        btnAddWater.setOnClickListener(v -> viewModel.addWater());

        // --- 3. Goal Views ---
        weightTargetView = view.findViewById(R.id.view_weight_target);
        tvGoalSummary = view.findViewById(R.id.tv_goal_summary);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
    }

    private void observeData() {
        // --- 1. Nutrition Logic ---
        // 'dailyTargets' contains the calculated needs based on Goal + Profile
        viewModel.getDailyTargets().observe(getViewLifecycleOwner(), targets -> {
            if (targets != null) {
                // When targets update (e.g. user changes goal), refresh the UI bars
                updateNutritionUI(targets);
            }
        });

        // Trigger updates when actual food logs change
        viewModel.getTodayCalories().observe(getViewLifecycleOwner(), v -> refreshProgress());
        viewModel.getTodayProtein().observe(getViewLifecycleOwner(), v -> refreshProgress());
        viewModel.getTodayCarbs().observe(getViewLifecycleOwner(), v -> refreshProgress());
        viewModel.getTodayFat().observe(getViewLifecycleOwner(), v -> refreshProgress());

        // --- 2. Water Logic ---
        viewModel.getWaterIntake().observe(getViewLifecycleOwner(), glasses -> {
            // Assuming 250ml per glass
            int currentMl = glasses * 250;
            // Get target from VM if available, else default 2500
            int targetMl = (viewModel.getDailyTargets().getValue() != null) ?
                    viewModel.getDailyTargets().getValue().waterMl : 2500;

            tvWaterCount.setText(glasses + " Glasses (" + currentMl + " / " + targetMl + "ml)");
        });

        // --- 3. Goal Logic ---
        // We need both the Goal (Target) and Latest Weight (Current) to draw the line
        viewModel.getActiveGoal().observe(getViewLifecycleOwner(), goal -> updateGoalUI());
        viewModel.getLatestWeight().observe(getViewLifecycleOwner(), weight -> updateGoalUI());
    }

    /**
     * Helper to redraw progress bars using the most recent Target data.
     */
    private void refreshProgress() {
        if (viewModel.getDailyTargets().getValue() != null) {
            updateNutritionUI(viewModel.getDailyTargets().getValue());
        }
    }

    /**
     * Updates the text and progress bars for Calories and Macros.
     */
    private void updateNutritionUI(HealthCalculator.MacroNutrients targets) {
        // Get Actuals (Safe Null Checks)
        int curCal = viewModel.getTodayCalories().getValue() != null ? viewModel.getTodayCalories().getValue() : 0;
        double curPro = viewModel.getTodayProtein().getValue() != null ? viewModel.getTodayProtein().getValue() : 0;
        double curCarb = viewModel.getTodayCarbs().getValue() != null ? viewModel.getTodayCarbs().getValue() : 0;
        double curFat = viewModel.getTodayFat().getValue() != null ? viewModel.getTodayFat().getValue() : 0;

        // Update Text Labels
        tvCalVal.setText(curCal + " / " + targets.calories);
        tvProteinVal.setText((int)curPro + "g / " + targets.protein + "g");
        tvCarbsVal.setText((int)curCarb + "g / " + targets.carbs + "g");
        tvFatVal.setText((int)curFat + "g / " + targets.fat + "g");

        // Update Progress Bars (Protect against divide by zero)
        progCal.setProgress(targets.calories > 0 ? (int)((double)curCal/targets.calories * 100) : 0);
        progProtein.setProgress(targets.protein > 0 ? (int)(curPro/targets.protein * 100) : 0);
        progCarbs.setProgress(targets.carbs > 0 ? (int)(curCarb/targets.carbs * 100) : 0);
        progFat.setProgress(targets.fat > 0 ? (int)(curFat/targets.fat * 100) : 0);
    }

    /**
     * Updates the Custom View (Line) and Summary Text for the Goal.
     */
    private void updateGoalUI() {
        Goal goal = viewModel.getActiveGoal().getValue();
        WeightEntry currentEntry = viewModel.getLatestWeight().getValue();
        UserProfile profile = viewModel.getUserProfile().getValue();

        if (goal != null && currentEntry != null && profile != null) {
            // Draw the line
            if (weightTargetView != null) {
                weightTargetView.setData(
                        goal.getStartWeight(),
                        currentEntry.getWeight(),
                        goal.getTargetWeight(),
                        profile.getHeight()
                );
            }

            // Update Summary Text
            double remaining = Math.abs(currentEntry.getWeight() - goal.getTargetWeight());
            String msg = String.format("%.1f kg left to reach goal", remaining);

            // Check if goal reached (Logic handles both Gain and Lose scenarios)
            boolean goalReached = (goal.getStartWeight() > goal.getTargetWeight() && currentEntry.getWeight() <= goal.getTargetWeight()) ||
                    (goal.getStartWeight() < goal.getTargetWeight() && currentEntry.getWeight() >= goal.getTargetWeight());

            if (goalReached) {
                msg = "Goal Reached! 🎉";
                if(tvGoalSummary != null) tvGoalSummary.setTextColor(getResources().getColor(R.color.success_blue, null));
            } else {
                if(tvGoalSummary != null) tvGoalSummary.setTextColor(getResources().getColor(R.color.text_hint, null));
            }

            if(tvGoalSummary != null) tvGoalSummary.setText(msg);

        } else {
            // Default state if no data
            if(tvGoalSummary != null) tvGoalSummary.setText("Set a goal and log weight to see progress");
            if (weightTargetView != null) weightTargetView.setData(0,0,0,0);
        }
    }
}