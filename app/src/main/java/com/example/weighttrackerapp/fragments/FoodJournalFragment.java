package com.example.weighttrackerapp.fragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weighttrackerapp.R;
import com.example.weighttrackerapp.models.FoodEntry;
import com.example.weighttrackerapp.models.FoodItem;
import com.example.weighttrackerapp.viewmodels.FoodJournalViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodJournalFragment extends Fragment {

    private FoodJournalViewModel viewModel;
    private EditText etSearch;
    private RecyclerView rvResults;
    private SearchAdapter adapter;
    private TextView tvSelectionCount;
    private Button btnFinishMeal;
    private Button btnCreateCustom;
    // Selection Data
    private final Map<Integer, Double> selectedQuantities = new HashMap<>();
    private final Map<Integer, FoodItem> selectedItemsMap = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food_journal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(FoodJournalViewModel.class);

        // Bind Views
        etSearch = view.findViewById(R.id.et_search_food);
        rvResults = view.findViewById(R.id.rv_food_search_results);
        tvSelectionCount = view.findViewById(R.id.tv_selection_count);
        btnFinishMeal = view.findViewById(R.id.btn_finish_meal);
        btnCreateCustom = view.findViewById(R.id.btn_create_custom); // Critical: Must exist in XML

        setupRecyclerView();

        // Listeners
        if (etSearch != null) {
            etSearch.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                public void onTextChanged(CharSequence s, int start, int before, int count) { search(s.toString()); }
                public void afterTextChanged(Editable s) {}
            });
        }

        if (btnFinishMeal != null) {
            btnFinishMeal.setOnClickListener(v -> showMealTypeDialog());
        }

        // FIX: Ensure this isn't null before setting listener
        if (btnCreateCustom != null) {
            btnCreateCustom.setOnClickListener(v -> showCreateFoodDialog());
        }

        search("");
    }

    private void setupRecyclerView() {
        adapter = new SearchAdapter();
        rvResults.setLayoutManager(new LinearLayoutManager(getContext()));
        rvResults.setAdapter(adapter);
    }

    private void search(String query) {
        viewModel.searchFood(query).observe(getViewLifecycleOwner(), items -> {
            adapter.setItems(items);
        });
    }

    private void updateBottomBar() {
        int count = selectedQuantities.size();
        if (tvSelectionCount != null) tvSelectionCount.setText(count + " items selected");
        if (btnFinishMeal != null) btnFinishMeal.setEnabled(count > 0);
    }

    // --- DIALOGS ---

    private void showGramsDialog(FoodItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_food_quantity, null);

        EditText etGrams = view.findViewById(R.id.et_grams);
        View spContainer = view.findViewById(R.id.sp_meal_type);
        View tvLabel = view.findViewById(R.id.tv_meal_label);

        if(spContainer != null) spContainer.setVisibility(View.GONE);
        if(tvLabel != null) tvLabel.setVisibility(View.GONE);

        builder.setView(view)
                .setTitle(item.getName())
                .setMessage("Enter amount in grams:")
                .setPositiveButton("Confirm", (dialog, which) -> {
                    String gramsStr = etGrams.getText().toString();
                    if (!gramsStr.isEmpty()) {
                        double grams = Double.parseDouble(gramsStr);
                        selectedQuantities.put(item.getId(), grams);
                        selectedItemsMap.put(item.getId(), item);
                        updateBottomBar();
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showMealTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_food_quantity, null);

        // Hide grams input
        View tilGrams = view.findViewById(R.id.til_grams);
        if(tilGrams != null) tilGrams.setVisibility(View.GONE);

        Spinner spMeal = view.findViewById(R.id.sp_meal_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"Breakfast", "Lunch", "Dinner", "Snack"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMeal.setAdapter(adapter);

        builder.setView(view)
                .setTitle("Select Meal Type")
                .setPositiveButton("Save All", (dialog, which) -> {
                    String mealType = spMeal.getSelectedItem().toString();
                    saveAllEntries(mealType);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showCreateFoodDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_create_food, null);

        EditText etName = view.findViewById(R.id.et_new_name);
        EditText etCal = view.findViewById(R.id.et_new_cal);
        EditText etPro = view.findViewById(R.id.et_new_pro);
        EditText etCarb = view.findViewById(R.id.et_new_carb);
        EditText etFat = view.findViewById(R.id.et_new_fat);

        builder.setView(view)
                .setPositiveButton("Save Item", (dialog, which) -> {
                    if (etName.getText().toString().isEmpty() || etCal.getText().toString().isEmpty()) {
                        Toast.makeText(getContext(), "Name and Calories required", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        String name = etName.getText().toString();
                        int cal = Integer.parseInt(etCal.getText().toString());
                        double pro = parseDoubleSafe(etPro.getText().toString());
                        double carb = parseDoubleSafe(etCarb.getText().toString());
                        double fat = parseDoubleSafe(etFat.getText().toString());

                        viewModel.createCustomFood(name, cal, pro, carb, fat);
                        Toast.makeText(getContext(), "Created " + name, Toast.LENGTH_SHORT).show();
                        if(etSearch != null) etSearch.setText(name);

                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Invalid numbers", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void saveAllEntries(String mealType) {
        for (Integer id : selectedQuantities.keySet()) {
            FoodItem item = selectedItemsMap.get(id);
            Double grams = selectedQuantities.get(id);
            if (item != null && grams != null) {
                double ratio = grams / 100.0;
                int cal = (int) (item.getCalories() * ratio);
                double pro = item.getProtein() * ratio;
                double carb = item.getCarbs() * ratio;
                double fat = item.getFat() * ratio;

                FoodEntry entry = new FoodEntry(item.getName(), System.currentTimeMillis(), cal, pro, carb, fat, mealType);
                viewModel.insertFoodEntry(entry);
            }
        }
        Toast.makeText(getContext(), "Saved " + selectedQuantities.size() + " items", Toast.LENGTH_SHORT).show();
        selectedQuantities.clear();
        selectedItemsMap.clear();
        updateBottomBar();
        adapter.notifyDataSetChanged();
    }

    private double parseDoubleSafe(String val) {
        return (val == null || val.isEmpty()) ? 0.0 : Double.parseDouble(val);
    }

    // --- ADAPTER ---
    private class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.VH> {
        private List<FoodItem> items = new ArrayList<>();

        void setItems(List<FoodItem> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @NonNull @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_search, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            FoodItem item = items.get(position);
            holder.tvName.setText(item.getName());
            holder.tvInfo.setText(item.getCalories() + " kcal / 100g");

            if (selectedQuantities.containsKey(item.getId())) {
                Double grams = selectedQuantities.get(item.getId());
                holder.itemView.setBackgroundColor(Color.parseColor("#E8F5E9"));
                holder.ivCheck.setVisibility(View.VISIBLE);
                holder.tvInfo.setText("Selected: " + grams + "g");
                holder.tvInfo.setTextColor(Color.parseColor("#2E7D32"));
            } else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
                holder.ivCheck.setVisibility(View.GONE);
                holder.tvInfo.setTextColor(Color.GRAY);
            }

            holder.itemView.setOnClickListener(v -> {
                if (selectedQuantities.containsKey(item.getId())) {
                    selectedQuantities.remove(item.getId());
                    selectedItemsMap.remove(item.getId());
                    updateBottomBar();
                    notifyItemChanged(position);
                } else {
                    showGramsDialog(item);
                }
            });
        }

        @Override public int getItemCount() { return items.size(); }

        class VH extends RecyclerView.ViewHolder {
            TextView tvName, tvInfo;
            ImageView ivCheck;
            VH(View v) {
                super(v);
                tvName = v.findViewById(R.id.tv_name);
                tvInfo = v.findViewById(R.id.tv_info);
                ivCheck = v.findViewById(R.id.iv_check);
            }
        }
    }
}