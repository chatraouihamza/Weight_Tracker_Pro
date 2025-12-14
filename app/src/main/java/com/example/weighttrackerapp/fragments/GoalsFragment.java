package com.example.weighttrackerapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.weighttrackerapp.R;
import com.example.weighttrackerapp.viewmodels.GoalsViewModel;
import com.example.weighttrackerapp.adapters.GoalAdapter;
import com.example.weighttrackerapp.models.Goal;

import java.util.Date;

/**
 * Goals Fragment - Create and track personal health goals.
 */
public class GoalsFragment extends Fragment {
    
    private GoalsViewModel viewModel;
    private EditText etGoalTitle, etGoalDescription, etTargetWeight, etStartWeight;
    private Spinner spGoalType;
    private Button btnCreateGoal;
    private RecyclerView rvGoals;
    private GoalAdapter adapter;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_goals, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initializeViews(view);
        setupViewModel();
        setupRecyclerView();
        setupListeners();
        observeData();
    }
    
    private void initializeViews(View view) {
        etGoalTitle = view.findViewById(R.id.et_goal_title);
        etGoalDescription = view.findViewById(R.id.et_goal_description);
        etTargetWeight = view.findViewById(R.id.et_target_weight);
        etStartWeight = view.findViewById(R.id.et_start_weight);
        spGoalType = view.findViewById(R.id.sp_goal_type);
        btnCreateGoal = view.findViewById(R.id.btn_create_goal);
        rvGoals = view.findViewById(R.id.rv_goals);
    }
    
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(GoalsViewModel.class);
    }
    
    private void setupRecyclerView() {
        adapter = new GoalAdapter();
        rvGoals.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGoals.setAdapter(adapter);
    }
    
    private void setupListeners() {
        btnCreateGoal.setOnClickListener(v -> createGoal());
    }
    
    private void createGoal() {
        String title = etGoalTitle.getText().toString().trim();
        String description = etGoalDescription.getText().toString().trim();
        String targetWeightStr = etTargetWeight.getText().toString().trim();
        String startWeightStr = etStartWeight.getText().toString().trim();
        String goalType = spGoalType.getSelectedItem().toString();
        
        if (title.isEmpty() || targetWeightStr.isEmpty() || startWeightStr.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            double targetWeight = Double.parseDouble(targetWeightStr);
            double startWeight = Double.parseDouble(startWeightStr);
            
            Goal goal = new Goal(title, description, targetWeight, startWeight, 
                               new Date(), new Date(), goalType);
            viewModel.insertGoal(goal);
            
            // Clear fields
            etGoalTitle.setText("");
            etGoalDescription.setText("");
            etTargetWeight.setText("");
            etStartWeight.setText("");
            
            Toast.makeText(getContext(), "Goal created successfully", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid weight values", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void observeData() {
        viewModel.getActiveGoals().observe(getViewLifecycleOwner(), goals -> {
            adapter.submitList(goals);
        });
    }
}
