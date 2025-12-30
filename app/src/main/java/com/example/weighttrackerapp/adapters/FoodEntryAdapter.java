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

public class FoodEntryAdapter extends ListAdapter<FoodEntry, FoodEntryAdapter.ViewHolder> {

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(FoodEntry entry);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public FoodEntryAdapter() {
        super(new DiffUtil.ItemCallback<FoodEntry>() {
            @Override
            public boolean areItemsTheSame(@NonNull FoodEntry oldItem, @NonNull FoodEntry newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull FoodEntry oldItem, @NonNull FoodEntry newItem) {
                return oldItem.getCalories() == newItem.getCalories() &&
                        oldItem.getFoodName().equals(newItem.getFoodName());
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
        holder.bind(entry, listener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCals, tvMacros, tvType;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_food_name);
            tvCals = itemView.findViewById(R.id.tv_calories);
            tvMacros = itemView.findViewById(R.id.tv_macros);
            tvType = itemView.findViewById(R.id.tv_meal_type);
        }

        void bind(FoodEntry entry, OnItemClickListener listener) {
            tvName.setText(entry.getFoodName());
            tvCals.setText(entry.getCalories() + " kcal");
            tvMacros.setText(String.format("P:%.0f C:%.0f F:%.0f", entry.getProtein(), entry.getCarbs(), entry.getFat()));
            tvType.setText(entry.getMealType());

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(entry);
            });
        }
    }
}