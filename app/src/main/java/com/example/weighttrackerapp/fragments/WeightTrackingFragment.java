package com.example.weighttrackerapp.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.weighttrackerapp.R;
import com.example.weighttrackerapp.models.Goal;
import com.example.weighttrackerapp.models.Measurement;
import com.example.weighttrackerapp.models.UserProfile;
import com.example.weighttrackerapp.models.WeightEntry;
import com.example.weighttrackerapp.utils.FormatUtils;
import com.example.weighttrackerapp.utils.HealthCalculator;
import com.example.weighttrackerapp.viewmodels.DashboardViewModel;
import com.example.weighttrackerapp.viewmodels.WeightTrackingViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.chip.ChipGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeightTrackingFragment extends Fragment {

    private WeightTrackingViewModel viewModel;
    private DashboardViewModel dashboardViewModel;

    // UI
    private LineChart chart;
    private ChipGroup chipGroupTimeframe;
    private TextView tvBmi, tvChange, tvAvg, tvProgress;
    private TextView tvMeasWaist, tvMeasFat, tvMeasRatio;
    private LinearLayout layoutDateNav;
    private ImageButton btnPrev, btnNext;
    private TextView tvDateRangeLabel;
    private Button btnLogWeight, btnLogMeasure;

    // Data
    private List<WeightEntry> allEntries = new ArrayList<>();
    private Goal currentGoal;
    private UserProfile userProfile;

    // Time State
    private static final int SCOPE_1M = 1;
    private static final int SCOPE_6M = 2;
    private static final int SCOPE_1Y = 3;
    private static final int SCOPE_ALL = 4;

    private int currentScope = SCOPE_ALL;
    private Calendar calendarCursor;
    private Date tempDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weight_tracking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calendarCursor = Calendar.getInstance(); // Default to Now
        initializeViews(view);
        setupChartConfig();
        setupViewModels();
        setupListeners();
    }

    private void initializeViews(View view) {
        chart = view.findViewById(R.id.chart_weight);
        chipGroupTimeframe = view.findViewById(R.id.chip_group_timeframe);

        tvBmi = view.findViewById(R.id.tv_stat_bmi);
        tvChange = view.findViewById(R.id.tv_stat_change);
        tvAvg = view.findViewById(R.id.tv_stat_avg);
        tvProgress = view.findViewById(R.id.tv_stat_progress);

        tvMeasWaist = view.findViewById(R.id.tv_meas_waist);
        tvMeasFat = view.findViewById(R.id.tv_meas_fat);
        tvMeasRatio = view.findViewById(R.id.tv_meas_ratio);

        layoutDateNav = view.findViewById(R.id.layout_date_nav);
        btnPrev = view.findViewById(R.id.btn_prev_date);
        btnNext = view.findViewById(R.id.btn_next_date);
        tvDateRangeLabel = view.findViewById(R.id.tv_date_range_label);

        btnLogWeight = view.findViewById(R.id.btn_action_weight);
        btnLogMeasure = view.findViewById(R.id.btn_action_measure);
    }

    private void setupViewModels() {
        viewModel = new ViewModelProvider(this).get(WeightTrackingViewModel.class);
        dashboardViewModel = new ViewModelProvider(requireActivity()).get(DashboardViewModel.class);

        // Observer Weight
        viewModel.getAllWeightEntries().observe(getViewLifecycleOwner(), entries -> {
            if (entries != null) {
                // Sort by date ASC
                Collections.sort(entries, Comparator.comparingLong(WeightEntry::getDate));
                allEntries = entries;
                updateChartAndStats();
            }
        });

        viewModel.getActiveGoal().observe(getViewLifecycleOwner(), goal -> {
            currentGoal = goal;
            updateChartAndStats();
        });

        dashboardViewModel.getUserProfile().observe(getViewLifecycleOwner(), profile -> {
            userProfile = profile;
            updateChartAndStats();
        });

        viewModel.getLatestMeasurement().observe(getViewLifecycleOwner(), this::updateMeasurementUI);
    }

    private void setupChartConfig() {
        chart.setDescription(null);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);
        chart.setDrawGridBackground(false);
        chart.setExtraBottomOffset(10f);
        chart.getAxisRight().setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // FIX: PRECISION ISSUE
        // We will pass "Seconds" to the chart, so we need to multiply by 1000L to format back to Date
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                long timestamp = (long) value * 1000L; // Convert back to Millis
                return FormatUtils.formatChartDate(timestamp);
            }
        });
        xAxis.setLabelRotationAngle(-45);
    }

    private void setupListeners() {
        chipGroupTimeframe.setOnCheckedStateChangeListener((group, checkedIds) -> {
            int id = group.getCheckedChipId();
            // Reset cursor to Today whenever switching modes so user sees current data first
            calendarCursor = Calendar.getInstance();

            if (id == R.id.chip_1m) currentScope = SCOPE_1M;
            else if (id == R.id.chip_6m) currentScope = SCOPE_6M;
            else if (id == R.id.chip_1y) currentScope = SCOPE_1Y;
            else currentScope = SCOPE_ALL;

            updateChartAndStats();
        });

        if (btnPrev != null) btnPrev.setOnClickListener(v -> shiftDateWindow(-1));
        if (btnNext != null) btnNext.setOnClickListener(v -> shiftDateWindow(1));

        btnLogWeight.setOnClickListener(v -> showWeightDialog());
        btnLogMeasure.setOnClickListener(v -> showMeasurementDialog());
    }

    private void shiftDateWindow(int direction) {
        if (currentScope == SCOPE_1M) {
            calendarCursor.add(Calendar.MONTH, direction);
        } else if (currentScope == SCOPE_6M) {
            calendarCursor.add(Calendar.MONTH, direction * 6);
        } else if (currentScope == SCOPE_1Y) {
            calendarCursor.add(Calendar.YEAR, direction);
        }
        updateChartAndStats();
    }

    private void updateChartAndStats() {
        if (allEntries.isEmpty()) {
            chart.clear();
            return;
        }

        // 1. Calculate Window
        long startTime = 0;
        long endTime = Long.MAX_VALUE;
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy", Locale.getDefault());

        if (currentScope != SCOPE_ALL && layoutDateNav != null) {
            layoutDateNav.setVisibility(View.VISIBLE);

            Calendar startCal = (Calendar) calendarCursor.clone();
            Calendar endCal = (Calendar) calendarCursor.clone();

            if (currentScope == SCOPE_1M) {
                // Set to 1st day of the month
                startCal.set(Calendar.DAY_OF_MONTH, 1);
                startCal.set(Calendar.HOUR_OF_DAY, 0);
                startCal.set(Calendar.MINUTE, 0);

                // Set to End of Month
                endCal.set(Calendar.DAY_OF_MONTH, endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
                endCal.set(Calendar.HOUR_OF_DAY, 23);
                endCal.set(Calendar.MINUTE, 59);

                tvDateRangeLabel.setText(sdf.format(calendarCursor.getTime()));

            } else if (currentScope == SCOPE_6M) {
                // Go back 5 months + current month = 6 months
                startCal.add(Calendar.MONTH, -5);
                startCal.set(Calendar.DAY_OF_MONTH, 1);

                endCal.set(Calendar.DAY_OF_MONTH, endCal.getActualMaximum(Calendar.DAY_OF_MONTH));

                tvDateRangeLabel.setText(sdf.format(startCal.getTime()) + " - " + sdf.format(calendarCursor.getTime()));

            } else if (currentScope == SCOPE_1Y) {
                startCal.set(Calendar.DAY_OF_YEAR, 1);
                endCal.set(Calendar.MONTH, 11);
                endCal.set(Calendar.DAY_OF_MONTH, 31);

                SimpleDateFormat yearFmt = new SimpleDateFormat("yyyy", Locale.getDefault());
                tvDateRangeLabel.setText(yearFmt.format(calendarCursor.getTime()));
            }

            startTime = startCal.getTimeInMillis();
            endTime = endCal.getTimeInMillis();

        } else if (layoutDateNav != null) {
            layoutDateNav.setVisibility(View.GONE);
        }

        // 2. Filter
        List<WeightEntry> filteredList = new ArrayList<>();
        double sum = 0;
        for (WeightEntry w : allEntries) {
            if (w.getDate() >= startTime && w.getDate() <= endTime) {
                filteredList.add(w);
                sum += w.getWeight();
            }
        }

        if (filteredList.isEmpty()) {
            chart.clear();
            chart.setNoDataText("No data for " + tvDateRangeLabel.getText());
            chart.invalidate();
            tvAvg.setText("--");
            tvChange.setText("--");
            return;
        }

        // 3. Stats
        WeightEntry latest = filteredList.get(filteredList.size() - 1);
        WeightEntry oldest = filteredList.get(0);

        double avg = sum / filteredList.size();
        tvAvg.setText(FormatUtils.formatWeight(avg) + " kg");

        double change = latest.getWeight() - oldest.getWeight();
        String sign = change > 0 ? "+" : "";
        tvChange.setText(sign + FormatUtils.formatWeight(change) + " kg");

        int color = change <= 0 ? R.color.success_blue : R.color.warning_orange;
        if (currentGoal != null && currentGoal.getGoalType().equals(Goal.TYPE_GAIN)) {
            color = change >= 0 ? R.color.success_blue : R.color.warning_orange;
        }
        tvChange.setTextColor(getResources().getColor(color, null));

        if (userProfile != null && userProfile.getHeight() > 0) {
            double bmi = HealthCalculator.calculateBMI(latest.getWeight(), userProfile.getHeight());
            tvBmi.setText(FormatUtils.formatBMI(bmi));
        }

        if (currentGoal != null) tvProgress.setText(currentGoal.getProgressPercentage() + "%");
        else tvProgress.setText("N/A");

        // 4. Draw Chart (Without Prediction)
        drawChart(filteredList);
    }

    private void drawChart(List<WeightEntry> filteredList) {
        List<Entry> realValues = new ArrayList<>();
        for (WeightEntry w : filteredList) {
            // FIX: Convert Millis (Long) to Seconds (Float) to preserve precision
            float xValue = w.getDate() / 1000f;
            realValues.add(new Entry(xValue, (float) w.getWeight()));
        }

        LineDataSet setReal = new LineDataSet(realValues, "Weight");
        setReal.setColor(getResources().getColor(R.color.primary_green, null));
        setReal.setCircleColor(getResources().getColor(R.color.primary_green, null));
        setReal.setLineWidth(3f);
        setReal.setDrawValues(false);
        setReal.setDrawCircles(filteredList.size() < 15); // Show dots if few items
        setReal.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        setReal.setDrawFilled(true);
        setReal.setFillColor(getResources().getColor(R.color.primary_light, null));

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(setReal);
        // Removed Prediction Line Set

        LineData data = new LineData(sets);
        chart.setData(data);

        // Refresh View
        chart.fitScreen();
        chart.notifyDataSetChanged();
        chart.invalidate();
        chart.animateX(500);
    }

    // --- DIALOGS ---
    private void showWeightDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_weight, null);

        EditText etW = view.findViewById(R.id.et_dialog_weight);
        Button btnD = view.findViewById(R.id.btn_dialog_date);

        tempDate = new Date();
        btnD.setText("Date: " + FormatUtils.formatDate(tempDate));

        btnD.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            c.setTime(tempDate);
            new DatePickerDialog(getContext(), (view1, year, month, dayOfMonth) -> {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                tempDate = newDate.getTime();
                btnD.setText("Date: " + FormatUtils.formatDate(tempDate));
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        builder.setView(view)
                .setPositiveButton("Save", (dialog, which) -> {
                    try {
                        double weight = Double.parseDouble(etW.getText().toString());
                        viewModel.addNewWeight(weight, tempDate.getTime(), "");
                        Toast.makeText(getContext(), "Weight Logged", Toast.LENGTH_SHORT).show();
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Invalid weight", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showMeasurementDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_measurement, null);

        EditText etWaist = view.findViewById(R.id.et_waist);
        EditText etHips = view.findViewById(R.id.et_hips);
        EditText etFat = view.findViewById(R.id.et_bodyfat);

        builder.setView(view)
                .setPositiveButton("Save", (dialog, which) -> {
                    try {
                        double waist = parseDouble(etWaist.getText().toString());
                        double hips = parseDouble(etHips.getText().toString());
                        double fat = parseDouble(etFat.getText().toString());

                        if (waist > 0) {
                            viewModel.addMeasurement(waist, hips, fat);
                            Toast.makeText(getContext(), "Stats Updated", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateMeasurementUI(Measurement m) {
        if (m != null) {
            tvMeasWaist.setText(FormatUtils.formatWeight(m.getWaist()) + " cm");
            tvMeasFat.setText(m.getBodyFat() > 0 ? FormatUtils.formatWeight(m.getBodyFat()) + " %" : "--");
            double ratio = m.getWaistToHipRatio();
            if (ratio > 0) {
                String risk = ratio > 0.90 ? "High Risk" : (ratio > 0.85 ? "Mod. Risk" : "Healthy");
                tvMeasRatio.setText(String.format("%.2f (%s)", ratio, risk));
            } else {
                tvMeasRatio.setText("--");
            }
        }
    }

    private double parseDouble(String val) {
        if (val == null || val.isEmpty()) return 0.0;
        return Double.parseDouble(val);
    }
}