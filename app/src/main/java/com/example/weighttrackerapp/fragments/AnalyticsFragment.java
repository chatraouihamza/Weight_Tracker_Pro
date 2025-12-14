package com.example.weighttrackerapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.weighttrackerapp.R;
import com.example.weighttrackerapp.viewmodels.AnalyticsViewModel;
import com.example.weighttrackerapp.utils.FormatUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

/**
 * Analytics Fragment - Detailed charts and statistics.
 */
public class AnalyticsFragment extends Fragment {
    
    private AnalyticsViewModel viewModel;
    private LineChart chartWeightProgress;
    private TextView tvAverageWeight, tvWeightLoss, tvGoalProgress;
    private TextView tvBMITrend, tvCalorieAverage;
    private LinearLayout llStats;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_analytics, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initializeViews(view);
        setupViewModel();
        setupChart();
        observeData();
    }
    
    private void initializeViews(View view) {
        chartWeightProgress = view.findViewById(R.id.chart_weight_progress);
        tvAverageWeight = view.findViewById(R.id.tv_average_weight);
        tvWeightLoss = view.findViewById(R.id.tv_weight_loss);
        tvGoalProgress = view.findViewById(R.id.tv_goal_progress);
        tvBMITrend = view.findViewById(R.id.tv_bmi_trend);
        tvCalorieAverage = view.findViewById(R.id.tv_calorie_average);
        llStats = view.findViewById(R.id.ll_stats);
    }
    
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(AnalyticsViewModel.class);
    }
    
    private void setupChart() {
        chartWeightProgress.setDescription(null);
        chartWeightProgress.setTouchEnabled(true);
        chartWeightProgress.setDragEnabled(true);
        chartWeightProgress.setScaleEnabled(true);
        chartWeightProgress.setPinchZoom(true);
    }
    
    private void observeData() {
        // Observe weight progress data
        viewModel.getWeightProgressData().observe(getViewLifecycleOwner(), entries -> {
            if (entries != null && !entries.isEmpty()) {
                displayWeightChart(entries);
            }
        });
        
        // Observe statistics
        viewModel.getAverageWeight().observe(getViewLifecycleOwner(), avg -> {
            if (avg != null) {
                tvAverageWeight.setText("Avg: " + FormatUtils.formatWeight(avg));
            }
        });
        
        viewModel.getWeightLoss().observe(getViewLifecycleOwner(), loss -> {
            if (loss != null) {
                String lossText = (loss <= 0 ? "" : "-") + FormatUtils.formatWeight(Math.abs(loss));
                tvWeightLoss.setText("Loss: " + lossText);
            }
        });
        
        viewModel.getBMITrend().observe(getViewLifecycleOwner(), trend -> {
            if (trend != null) {
                tvBMITrend.setText("BMI Trend: " + trend);
            }
        });
        
        viewModel.getAverageCalories().observe(getViewLifecycleOwner(), avg -> {
            if (avg != null) {
                tvCalorieAverage.setText("Avg Calories: " + avg + " kcal");
            }
        });
    }
    
    private void displayWeightChart(List<Entry> entries) {
        LineDataSet dataSet = new LineDataSet(entries, "Weight Progress");
        dataSet.setColor(getResources().getColor(R.color.primary_blue));
        dataSet.setValueTextColor(getResources().getColor(R.color.text_primary));
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        
        LineData lineData = new LineData(dataSet);
        chartWeightProgress.setData(lineData);
        chartWeightProgress.invalidate();
    }
}
