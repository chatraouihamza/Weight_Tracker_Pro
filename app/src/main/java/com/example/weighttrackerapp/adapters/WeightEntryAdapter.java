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
import com.example.weighttrackerapp.models.WeightEntry;
import com.example.weighttrackerapp.utils.FormatUtils;

public class WeightEntryAdapter extends ListAdapter<WeightEntry, WeightEntryAdapter.ViewHolder> {

    public WeightEntryAdapter() {
        super(new DiffUtil.ItemCallback<WeightEntry>() {
            @Override
            public boolean areItemsTheSame(@NonNull WeightEntry oldItem, @NonNull WeightEntry newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull WeightEntry oldItem, @NonNull WeightEntry newItem) {
                // Logic: Compare Weight and Date (Long)
                return oldItem.getWeight() == newItem.getWeight() &&
                        oldItem.getDate() == newItem.getDate();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weight_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeightEntry entry = getItem(position);
        holder.bind(entry);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDate;
        private final TextView tvWeight;
        // Removed tvNotes

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvWeight = itemView.findViewById(R.id.tv_weight);
            // Removed findViewById for notes
        }

        void bind(WeightEntry entry) {
            // Format Date (Long -> String)
            tvDate.setText(FormatUtils.formatDate(entry.getDate()));

            // Format Weight
            tvWeight.setText(FormatUtils.formatWeight(entry.getWeight()) + " kg");

            // Removed Notes Logic
        }
    }
}