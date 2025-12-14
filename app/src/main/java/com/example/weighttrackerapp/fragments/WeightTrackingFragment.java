package com.example.weighttrackerapp.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.weighttrackerapp.R;
import com.example.weighttrackerapp.viewmodels.WeightTrackingViewModel;
import com.example.weighttrackerapp.adapters.WeightEntryAdapter;
import com.example.weighttrackerapp.models.WeightEntry;

import java.util.Calendar;
import java.util.Date;

/**
 * Weight Tracking Fragment - Log and view weight entries.
 */
public class WeightTrackingFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    
    private WeightTrackingViewModel viewModel;
    private EditText etWeight, etNotes;
    private Button btnAddWeight, btnSelectDate;
    private RecyclerView rvWeightEntries;
    private WeightEntryAdapter adapter;
    private Date selectedDate;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weight_tracking, container, false);
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
        etWeight = view.findViewById(R.id.et_weight);
        etNotes = view.findViewById(R.id.et_notes);
        btnAddWeight = view.findViewById(R.id.btn_add_weight);
        btnSelectDate = view.findViewById(R.id.btn_select_date);
        rvWeightEntries = view.findViewById(R.id.rv_weight_entries);
        selectedDate = new Date();
    }
    
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(WeightTrackingViewModel.class);
    }
    
    private void setupRecyclerView() {
        adapter = new WeightEntryAdapter();
        rvWeightEntries.setLayoutManager(new LinearLayoutManager(getContext()));
        rvWeightEntries.setAdapter(adapter);
    }
    
    private void setupListeners() {
        btnAddWeight.setOnClickListener(v -> addWeightEntry());
        btnSelectDate.setOnClickListener(v -> showDatePicker());
    }
    
    private void addWeightEntry() {
        String weightStr = etWeight.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();
        
        if (weightStr.isEmpty()) {
            Toast.makeText(getContext(), "Please enter weight", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            double weight = Double.parseDouble(weightStr);
            WeightEntry entry = new WeightEntry(weight, selectedDate, notes);
            viewModel.insertWeightEntry(entry);
            
            etWeight.setText("");
            etNotes.setText("");
            selectedDate = new Date();
            
            Toast.makeText(getContext(), "Weight entry added successfully", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid weight value", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        
        new DatePickerDialog(getContext(), this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        selectedDate = calendar.getTime();
        btnSelectDate.setText("Date: " + android.text.format.DateFormat.format("dd/MM/yyyy", selectedDate));
    }
    
    private void observeData() {
        viewModel.getAllWeightEntries().observe(getViewLifecycleOwner(), entries -> {
            adapter.submitList(entries);
        });
    }
}
