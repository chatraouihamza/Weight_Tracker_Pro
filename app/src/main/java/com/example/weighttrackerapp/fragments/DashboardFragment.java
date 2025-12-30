package com.example.weighttrackerapp.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.weighttrackerapp.R;
import com.example.weighttrackerapp.adapters.MealAdapter;
import com.example.weighttrackerapp.models.FoodEntry;
import com.example.weighttrackerapp.models.Goal;
import com.example.weighttrackerapp.models.MealGroup;
import com.example.weighttrackerapp.models.UserProfile;
import com.example.weighttrackerapp.models.WeightEntry;
import com.example.weighttrackerapp.utils.HealthCalculator;
import com.example.weighttrackerapp.utils.WeightTargetView;
import com.example.weighttrackerapp.viewmodels.DashboardViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;

public class DashboardFragment extends Fragment {

    private DashboardViewModel viewModel;

    // UI Components - Nutrition
    private TextView tvCalVal, tvProteinVal, tvCarbsVal, tvFatVal;
    private LinearProgressIndicator progCal, progProtein, progCarbs, progFat;

    // UI Components - Water
    private TextView tvWaterCount;
    private View btnAddWater;
    private View cardWater;

    // UI Components - Goal (Custom View)
    private TextView tvGoalSummary;
    private WeightTargetView weightTargetView;

    // UI Components - Food List
    private RecyclerView rvFood;
    private MealAdapter mealAdapter;



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

        // --- 2. Food List Views ---
        rvFood = view.findViewById(R.id.rv_today_food);
        mealAdapter = new MealAdapter();
        rvFood.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFood.setAdapter(mealAdapter);

        // --- 3. Water Views ---
        tvWaterCount = view.findViewById(R.id.tv_water_count);
        btnAddWater = view.findViewById(R.id.btn_add_water);
        cardWater = view.findViewById(R.id.card_water);
        if (cardWater == null) {
            // Fallback: find the LinearLayout holding the water info
            cardWater = tvWaterCount.getParent().getParent() instanceof View ? (View) tvWaterCount.getParent().getParent() : tvWaterCount;
        }
        cardWater.setOnClickListener(v -> showWaterDialog());
        btnAddWater.setOnClickListener(v -> {
            viewModel.addWater();
            // Optional: Show a tiny feedback toast
             Toast.makeText(getContext(), "+1 Cup", Toast.LENGTH_SHORT).show();
        });


        // --- 4. Goal Views ---
        weightTargetView = view.findViewById(R.id.view_weight_target);
        tvGoalSummary = view.findViewById(R.id.tv_goal_summary);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
    }

    private void observeData() {
        // --- 1. Nutrition Logic ---
        viewModel.getDailyTargets().observe(getViewLifecycleOwner(), targets -> {
            if (targets != null) {
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
            int currentMl = glasses * 250;
            int targetMl = (viewModel.getDailyTargets().getValue() != null) ?
                    viewModel.getDailyTargets().getValue().waterMl : 2500;
            tvWaterCount.setText(glasses + " Glasses (" + currentMl + " / " + targetMl + "ml)");
        });

        // --- 3. Goal Logic ---
        viewModel.getActiveGoal().observe(getViewLifecycleOwner(), goal -> updateGoalUI());
        viewModel.getLatestWeight().observe(getViewLifecycleOwner(), weight -> updateGoalUI());

        // --- 4. Food List Logic ---
        // Observe Groups from ViewModel
        viewModel.getTodayMealGroups().observe(getViewLifecycleOwner(), groups -> {
            mealAdapter.submitList(groups);
        });

        mealAdapter.setOnMealClickListener(this::showMealDetailsDialog);
    }

    private void refreshProgress() {
        if (viewModel.getDailyTargets().getValue() != null) {
            updateNutritionUI(viewModel.getDailyTargets().getValue());
        }
    }

    private void updateNutritionUI(HealthCalculator.MacroNutrients targets) {
        int curCal = viewModel.getTodayCalories().getValue() != null ? viewModel.getTodayCalories().getValue() : 0;
        double curPro = viewModel.getTodayProtein().getValue() != null ? viewModel.getTodayProtein().getValue() : 0;
        double curCarb = viewModel.getTodayCarbs().getValue() != null ? viewModel.getTodayCarbs().getValue() : 0;
        double curFat = viewModel.getTodayFat().getValue() != null ? viewModel.getTodayFat().getValue() : 0;

        tvCalVal.setText(curCal + " / " + targets.calories);
        tvProteinVal.setText((int)curPro + "g / " + targets.protein + "g");
        tvCarbsVal.setText((int)curCarb + "g / " + targets.carbs + "g");
        tvFatVal.setText((int)curFat + "g / " + targets.fat + "g");

        progCal.setProgress(targets.calories > 0 ? (int)((double)curCal/targets.calories * 100) : 0);
        progProtein.setProgress(targets.protein > 0 ? (int)(curPro/targets.protein * 100) : 0);
        progCarbs.setProgress(targets.carbs > 0 ? (int)(curCarb/targets.carbs * 100) : 0);
        progFat.setProgress(targets.fat > 0 ? (int)(curFat/targets.fat * 100) : 0);
    }

    private void updateGoalUI() {
        Goal goal = viewModel.getActiveGoal().getValue();
        WeightEntry currentEntry = viewModel.getLatestWeight().getValue();
        UserProfile profile = viewModel.getUserProfile().getValue();

        if (goal != null && currentEntry != null && profile != null) {
            if (weightTargetView != null) {
                weightTargetView.setData(
                        goal.getStartWeight(),
                        currentEntry.getWeight(),
                        goal.getTargetWeight(),
                        profile.getHeight()
                );
            }

            double remaining = Math.abs(currentEntry.getWeight() - goal.getTargetWeight());
            String msg = String.format("%.1f kg left to reach goal", remaining);

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
            if(tvGoalSummary != null) tvGoalSummary.setText("Set a goal and log weight to see progress");
            if (weightTargetView != null) weightTargetView.setData(0,0,0,0);
        }
    }

    private void showMealDetailsDialog(MealGroup group) {
        List<FoodEntry> foods = group.getFoods();
        String[] itemNames = new String[foods.size()];

        for (int i = 0; i < foods.size(); i++) {
            itemNames[i] = foods.get(i).getFoodName() + " (" + foods.get(i).getCalories() + " kcal)";
        }

        new AlertDialog.Builder(getContext())
                .setTitle(group.getMealType() + " Details")
                .setItems(itemNames, (dialog, which) -> {
                    FoodEntry selectedEntry = foods.get(which);
                    showEditFoodDialog(selectedEntry);
                })
                .setPositiveButton("Close", null)
                .show();
    }

    private void showEditFoodDialog(FoodEntry entry) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_food_quantity, null);

        EditText etGrams = view.findViewById(R.id.et_grams);

        // UI Polish: Hide the Meal Type spinner (we just want to edit amount)
        View spMeal = view.findViewById(R.id.sp_meal_type);
        View tvMealLabel = view.findViewById(R.id.tv_meal_label); // Ensure ID exists in XML or use findViewById logic
        if (spMeal != null) spMeal.setVisibility(View.GONE);
        if (tvMealLabel != null) tvMealLabel.setVisibility(View.GONE);

        etGrams.setHint("New Amount (Grams)");
        // We don't know the previous grams exactly because we didn't store it,
        // so leaving it empty or "100" is safer than guessing.
        etGrams.setText("");

        builder.setView(view)
                .setTitle("Edit " + entry.getFoodName())
                .setMessage("Enter the corrected amount in grams:")
                .setPositiveButton("Update", (dialog, which) -> {
                    String val = etGrams.getText().toString();
                    if (!val.isEmpty()) {
                        try {
                            double newGrams = Double.parseDouble(val);
                            // CALL THE NEW SMART METHOD
                            viewModel.updateFoodEntryByGrams(entry, newGrams);
                        } catch (NumberFormatException e) {
                            // Handle error
                        }
                    }
                })
                .setNeutralButton("Delete", (dialog, which) -> {
                    viewModel.deleteFoodEntry(entry);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showWaterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_water_tracker, null);

        TextView tvCount = view.findViewById(R.id.tv_dialog_count);
        TextView tvRemaining = view.findViewById(R.id.tv_dialog_remaining);
        FloatingActionButton btnPlus = view.findViewById(R.id.btn_plus);
        FloatingActionButton btnMinus = view.findViewById(R.id.btn_minus);
        Button btnDone = view.findViewById(R.id.btn_done);

        AlertDialog dialog = builder.setView(view).create();

        // 1. Calculate Target (default 10 glasses / 2500ml)
        int targetMl = (viewModel.getDailyTargets().getValue() != null) ?
                viewModel.getDailyTargets().getValue().waterMl : 2500;
        int targetCups = (int) Math.ceil(targetMl / 250.0);

        // 2. Setup Updater Function
        Runnable updateDialogUI = () -> {
            int current = viewModel.getWaterIntake().getValue() != null ? viewModel.getWaterIntake().getValue() : 0;
            tvCount.setText(current + " Cups");

            int left = Math.max(0, targetCups - current);
            if (left > 0) {
                tvRemaining.setText("Goal: " + targetCups + " (" + left + " left)");
                tvRemaining.setTextColor(getResources().getColor(R.color.text_secondary, null));
            } else {
                tvRemaining.setText("Goal Reached! Great job! 💧");
                tvRemaining.setTextColor(getResources().getColor(R.color.primary_green, null));
            }
        };

        // 3. Initial State
        updateDialogUI.run();

        // 4. Listeners
        btnPlus.setOnClickListener(v -> {
            viewModel.addWater();
            updateDialogUI.run();
        });

        btnMinus.setOnClickListener(v -> {
            viewModel.removeWater();
            updateDialogUI.run();
        });

        btnDone.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        // Make background transparent for rounded corners effect
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }
}