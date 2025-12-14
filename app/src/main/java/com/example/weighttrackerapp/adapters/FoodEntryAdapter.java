package com.example.weighttrackerapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weighttrackerapp.R;
import com.example.weighttrackerapp.models.FoodEntry;

/**
 * Adapter for Food Entry RecyclerView.
 */
public class FoodEntryAdapter extends ListAdapter<FoodEntry, FoodEntryAdapter.ViewHolder> {
    
    public FoodEntryAdapter() {
        super(new DiffUtil.ItemCallback<FoodEntry>() {
            @Override
            public boolean areItemsTheSame(@NonNull FoodEntry oldItem, @NonNull FoodEntry newItem) {
                return oldItem.getId() == newItem.getId();
            }
            
            @Override
            public boolean areContentsTheSame(@NonNull FoodEntry oldItem, @NonNull FoodEntry newItem) {
                return oldItem.getFoodName().equals(newItem.getFoodName()) &&
                       oldItem.getCalories() == newItem.getCalories();
            }
        });
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food_entry, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodEntry entry = getItem(position);
        holder.bind(entry);
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvFoodName;
        private final TextView tvCalories;
        private final TextView tvMacros;
        private final TextView tvMealType;
        
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFoodName = itemView.findViewById(R.id.tv_food_name);
            tvCalories = itemView.findViewById(R.id.tv_calories);
            tvMacros = itemView.findViewById(R.id.tv_macros);
            tvMealType = itemView.findViewById(R.id.tv_meal_type);
        }
        
        void bind(FoodEntry entry) {
            tvFoodName.setText(entry.getFoodName());
            tvCalories.setText(entry.getCalories() + " kcal");
            tvMacros.setText(String.format("P: %.1fg | C: %.1fg | F: %.1fg", 
                    entry.getProtein(), entry.getCarbs(), entry.getFat()));
            tvMealType.setText(entry.getMealType());
        }
    }
}
