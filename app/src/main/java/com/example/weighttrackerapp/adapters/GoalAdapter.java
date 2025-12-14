package com.example.weighttrackerapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weighttrackerapp.R;
import com.example.weighttrackerapp.models.Goal;

/**
 * Adapter for Goal RecyclerView.
 */
public class GoalAdapter extends ListAdapter<Goal, GoalAdapter.ViewHolder> {
    
    public GoalAdapter() {
        super(new DiffUtil.ItemCallback<Goal>() {
            @Override
            public boolean areItemsTheSame(@NonNull Goal oldItem, @NonNull Goal newItem) {
                return oldItem.getId() == newItem.getId();
            }
            
            @Override
            public boolean areContentsTheSame(@NonNull Goal oldItem, @NonNull Goal newItem) {
                return oldItem.getTitle().equals(newItem.getTitle()) &&
                       oldItem.getProgressPercentage() == newItem.getProgressPercentage();
            }
        });
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_goal, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Goal goal = getItem(position);
        holder.bind(goal);
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvGoalTitle;
        private final TextView tvGoalDescription;
        private final ProgressBar pbGoalProgress;
        private final TextView tvProgress;
        
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGoalTitle = itemView.findViewById(R.id.tv_goal_title);
            tvGoalDescription = itemView.findViewById(R.id.tv_goal_description);
            pbGoalProgress = itemView.findViewById(R.id.pb_goal_progress);
            tvProgress = itemView.findViewById(R.id.tv_progress);
        }
        
        void bind(Goal goal) {
            tvGoalTitle.setText(goal.getTitle());
            tvGoalDescription.setText(goal.getDescription());
            pbGoalProgress.setProgress(goal.getProgressPercentage());
            tvProgress.setText(goal.getProgressPercentage() + "%");
        }
    }
}
