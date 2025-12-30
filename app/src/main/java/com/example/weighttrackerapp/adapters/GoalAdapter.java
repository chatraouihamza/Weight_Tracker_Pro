package com.example.weighttrackerapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weighttrackerapp.R;
import com.example.weighttrackerapp.models.Goal;
import com.example.weighttrackerapp.utils.FormatUtils;

public class GoalAdapter extends ListAdapter<Goal, GoalAdapter.GoalViewHolder> {

    private OnGoalClickListener listener;

    public interface OnGoalClickListener {
        void onGoalClick(Goal goal);
    }

    public void setOnGoalClickListener(OnGoalClickListener listener) {
        this.listener = listener;
    }

    public GoalAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Goal> DIFF_CALLBACK = new DiffUtil.ItemCallback<Goal>() {
        @Override
        public boolean areItemsTheSame(@NonNull Goal oldItem, @NonNull Goal newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Goal oldItem, @NonNull Goal newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getProgressPercentage() == newItem.getProgressPercentage() &&
                    oldItem.isCompleted() == newItem.isCompleted();
        }
    };

    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_goal, parent, false);
        return new GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
        Goal goal = getItem(position);
        holder.bind(goal);
    }

    class GoalViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvDescription;
        private final TextView tvDate; // Display target date
        private final ProgressBar pbProgress;
        private final TextView tvPercentage;

        public GoalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_goal_title);
            tvDescription = itemView.findViewById(R.id.tv_goal_description);
            // Ensure this ID exists in item_goal.xml, or remove this line
            tvDate = itemView.findViewById(R.id.tv_date);
            pbProgress = itemView.findViewById(R.id.pb_goal_progress);
            tvPercentage = itemView.findViewById(R.id.tv_progress);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onGoalClick(getItem(position));
                }
            });
        }

        public void bind(Goal goal) {
            tvTitle.setText(goal.getTitle());

            if(tvDate != null) {
                tvDate.setText("Deadline: " + FormatUtils.formatDate(goal.getTargetDate()));
            }

            if (goal.getDescription() != null && !goal.getDescription().isEmpty()) {
                tvDescription.setText(goal.getDescription());
                tvDescription.setVisibility(View.VISIBLE);
            } else {
                tvDescription.setVisibility(View.GONE);
            }

            pbProgress.setProgress(goal.getProgressPercentage());
            tvPercentage.setText(goal.getProgressPercentage() + "%");
        }
    }
}