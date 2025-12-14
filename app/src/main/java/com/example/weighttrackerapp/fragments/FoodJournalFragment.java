package com.example.weighttrackerapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.weighttrackerapp.R;
import com.example.weighttrackerapp.viewmodels.FoodJournalViewModel;
import com.example.weighttrackerapp.adapters.FoodEntryAdapter;
import com.example.weighttrackerapp.models.FoodEntry;
import com.example.weighttrackerapp.utils.HealthCalculator;

import java.util.Date;

/**
 * Food Journal Fragment - Track daily food intake and nutrition.
 */
public class FoodJournalFragment extends Fragment {
    
    private FoodJournalViewModel viewModel;
    private EditText etFoodName, etCalories, etProtein, etCarbs, etFat;
    private Spinner spMealType;
    private Button btnAddFood;
    private RecyclerView rvFoodEntries;
    private FoodEntryAdapter adapter;
    private TextView tvDailyCalories, tvDailyProtein, tvDailyCarbs, tvDailyFat;
    private TextView tvMacroGoals;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food_journal, container, false);
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
        etFoodName = view.findViewById(R.id.et_food_name);
        etCalories = view.findViewById(R.id.et_calories);
        etProtein = view.findViewById(R.id.et_protein);
        etCarbs = view.findViewById(R.id.et_carbs);
        etFat = view.findViewById(R.id.et_fat);
        spMealType = view.findViewById(R.id.sp_meal_type);
        btnAddFood = view.findViewById(R.id.btn_add_food);
        rvFoodEntries = view.findViewById(R.id.rv_food_entries);
        tvDailyCalories = view.findViewById(R.id.tv_daily_calories);
        tvDailyProtein = view.findViewById(R.id.tv_daily_protein);
        tvDailyCarbs = view.findViewById(R.id.tv_daily_carbs);
        tvDailyFat = view.findViewById(R.id.tv_daily_fat);
        tvMacroGoals = view.findViewById(R.id.tv_macro_goals);
    }
    
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(FoodJournalViewModel.class);
    }
    
    private void setupRecyclerView() {
        adapter = new FoodEntryAdapter();
        rvFoodEntries.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFoodEntries.setAdapter(adapter);
    }
    
    private void setupListeners() {
        btnAddFood.setOnClickListener(v -> addFoodEntry());
    }
    
    private void addFoodEntry() {
        String foodName = etFoodName.getText().toString().trim();
        String caloriesStr = etCalories.getText().toString().trim();
        String proteinStr = etProtein.getText().toString().trim();
        String carbsStr = etCarbs.getText().toString().trim();
        String fatStr = etFat.getText().toString().trim();
        String mealType = spMealType.getSelectedItem().toString();
        
        if (foodName.isEmpty() || caloriesStr.isEmpty()) {
            Toast.makeText(getContext(), "Please enter food name and calories", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            int calories = Integer.parseInt(caloriesStr);
            double protein = proteinStr.isEmpty() ? 0 : Double.parseDouble(proteinStr);
            double carbs = carbsStr.isEmpty() ? 0 : Double.parseDouble(carbsStr);
            double fat = fatStr.isEmpty() ? 0 : Double.parseDouble(fatStr);
            
            FoodEntry entry = new FoodEntry(foodName, new Date(), calories, protein, carbs, fat, mealType);
            viewModel.insertFoodEntry(entry);
            
            // Clear fields
            etFoodName.setText("");
            etCalories.setText("");
            etProtein.setText("");
            etCarbs.setText("");
            etFat.setText("");
            
            Toast.makeText(getContext(), "Food entry added", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid values", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void observeData() {
        // Observe today's food entries
        viewModel.getTodayFoodEntries().observe(getViewLifecycleOwner(), entries -> {
            adapter.submitList(entries);
        });
        
        // Observe daily calories
        viewModel.getDailyCalories().observe(getViewLifecycleOwner(), calories -> {
            tvDailyCalories.setText("Total: " + calories + " kcal");
        });
        
        // Observe daily macros
        viewModel.getDailyMacros().observe(getViewLifecycleOwner(), macros -> {
            if (macros != null && macros.length == 3) {
                tvDailyProtein.setText(String.format("Protein: %.1f g", macros[0]));
                tvDailyCarbs.setText(String.format("Carbs: %.1f g", macros[1]));
                tvDailyFat.setText(String.format("Fat: %.1f g", macros[2]));
            }
        });
    }
}
