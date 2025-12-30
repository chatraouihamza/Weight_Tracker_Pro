package com.example.weighttrackerapp.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weighttrackerapp.R;
import com.example.weighttrackerapp.adapters.GoalAdapter;
import com.example.weighttrackerapp.models.Goal;
import com.example.weighttrackerapp.models.UserProfile;
import com.example.weighttrackerapp.models.WeightEntry;
import com.example.weighttrackerapp.utils.FormatUtils;
import com.example.weighttrackerapp.viewmodels.GoalsViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class GoalsFragment extends Fragment {

    private GoalsViewModel viewModel;

    private TextView tvActiveCount, tvCompletedCount;
    private RecyclerView rvActive, rvCompleted;
    private GoalAdapter activeAdapter, completedAdapter;
    private ExtendedFloatingActionButton fabAdd;
    private UserProfile currentUserProfile;

    // Data needed for new goal
    private WeightEntry currentWeightEntry;
    private long tempTargetDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_goals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
        setupRecyclerViews();
        setupViewModel();

        fabAdd.setOnClickListener(v -> showAddGoalDialog());
    }

    private void initializeViews(View view) {
        tvActiveCount = view.findViewById(R.id.tv_active_count);
        tvCompletedCount = view.findViewById(R.id.tv_completed_count);
        rvActive = view.findViewById(R.id.rv_active_goals);
        rvCompleted = view.findViewById(R.id.rv_completed_goals);
        fabAdd = view.findViewById(R.id.fab_add_goal);
    }

    private void setupRecyclerViews() {
        activeAdapter = new GoalAdapter();
        rvActive.setLayoutManager(new LinearLayoutManager(getContext()));
        rvActive.setAdapter(activeAdapter);

        completedAdapter = new GoalAdapter();
        rvCompleted.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCompleted.setAdapter(completedAdapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(GoalsViewModel.class);

        // Active Goals
        viewModel.getActiveGoals().observe(getViewLifecycleOwner(), goals -> {
            activeAdapter.submitList(goals);
            tvActiveCount.setText(String.valueOf(goals != null ? goals.size() : 0));
        });

        // Completed Goals
        viewModel.getCompletedGoals().observe(getViewLifecycleOwner(), goals -> {
            completedAdapter.submitList(goals);
            tvCompletedCount.setText(String.valueOf(goals != null ? goals.size() : 0));
        });

        // Get Latest Weight (For Start Weight)
        viewModel.getLatestWeight().observe(getViewLifecycleOwner(), entry -> {
            currentWeightEntry = entry;
        });

    }

    private void showAddGoalDialog() {
        double startWeightValue = 0;

        if (currentWeightEntry == null) {
            Toast.makeText(getContext(), "Please log your current weight first!", Toast.LENGTH_LONG).show();
            return;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_goal, null);

        TextInputEditText etTitle = view.findViewById(R.id.et_dialog_title);
        TextInputEditText etDesc = view.findViewById(R.id.et_dialog_desc);
        TextInputEditText etTarget = view.findViewById(R.id.et_dialog_target);
        Button btnDate = view.findViewById(R.id.btn_dialog_date);

        // Default Date: 30 days from now
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, 30);
        tempTargetDate = c.getTimeInMillis();
        btnDate.setText("Target: " + FormatUtils.formatDate(tempTargetDate));

        btnDate.setOnClickListener(v -> {
            DatePickerDialog dpd = new DatePickerDialog(getContext(), (view1, year, month, dayOfMonth) -> {
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, dayOfMonth);
                tempTargetDate = cal.getTimeInMillis();
                btnDate.setText("Target: " + FormatUtils.formatDate(tempTargetDate));
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            dpd.getDatePicker().setMinDate(System.currentTimeMillis());
            dpd.show();
        });

        builder.setView(view)
                .setPositiveButton("Create", (dialog, which) -> {
                    String title = etTitle.getText().toString();
                    String desc = etDesc.getText().toString();
                    String targetStr = etTarget.getText().toString();

                    if (!title.isEmpty() && !targetStr.isEmpty()) {
                        createGoal(title, desc, Double.parseDouble(targetStr));
                    } else {
                        Toast.makeText(getContext(), "Title and Target Weight required", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void createGoal(String title, String desc, double targetWeight) {
        double startWeight = currentWeightEntry.getWeight();

        // Auto-Detect Type
        String type;
        if (targetWeight < startWeight) type = Goal.TYPE_LOSE;
        else if (targetWeight > startWeight) type = Goal.TYPE_GAIN;
        else type = Goal.TYPE_MAINTAIN;

        Goal goal = new Goal(title, desc, startWeight, targetWeight, tempTargetDate, type);
        viewModel.addNewGoal(goal);

        Toast.makeText(getContext(), "Goal Set: " + type, Toast.LENGTH_SHORT).show();
    }
}