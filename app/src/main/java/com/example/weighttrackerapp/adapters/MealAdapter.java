package com.example.weighttrackerapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weighttrackerapp.R;
import com.example.weighttrackerapp.models.MealGroup;

import java.util.ArrayList;
import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.ViewHolder> {

    private List<MealGroup> meals = new ArrayList<>();
    private OnMealClickListener listener;

    public interface OnMealClickListener {
        void onMealClick(MealGroup mealGroup);
    }

    public void setOnMealClickListener(OnMealClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<MealGroup> list) {
        this.meals = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // We can reuse item_food_entry.xml or create a simple one.
        // Let's assume reuse but we change what text goes where.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MealGroup meal = meals.get(position);
        holder.bind(meal, listener);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc, tvCals, tvType;

        ViewHolder(View itemView) {
            super(itemView);
            // Reusing IDs from item_food_entry
            tvTitle = itemView.findViewById(R.id.tv_food_name); // Will show Meal Type (Breakfast)
            tvCals = itemView.findViewById(R.id.tv_calories);   // Total Cals
            tvDesc = itemView.findViewById(R.id.tv_macros);     // Will show "Apple + Banana"
            tvType = itemView.findViewById(R.id.tv_meal_type);  // Hide this small label
        }

        void bind(MealGroup meal, OnMealClickListener listener) {
            tvTitle.setText(meal.getMealType()); // "Breakfast"
            tvCals.setText(meal.getTotalCalories() + " kcal");

            // Description: "Egg + Toast"
            tvDesc.setText(meal.getFoodDescription());

            // Hide the small tag since the main title is the type
            tvType.setVisibility(View.GONE);

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onMealClick(meal);
            });
        }
    }
}