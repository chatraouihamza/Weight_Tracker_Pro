package com.example.weighttrackerapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "goals")
public class Goal implements Serializable {

    public static final String TYPE_LOSE = "Lose Weight";
    public static final String TYPE_GAIN = "Gain Weight";
    public static final String TYPE_MAINTAIN = "Maintain";

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "user_id")
    private int userId;
    private String title;
    private String description;

    @ColumnInfo(name = "start_weight")
    private double startWeight;

    @ColumnInfo(name = "target_weight")
    private double targetWeight;

    // CHANGED: Use long timestamps
    @ColumnInfo(name = "start_date")
    private long startDate;

    @ColumnInfo(name = "target_date")
    private long targetDate;

    @ColumnInfo(name = "goal_type")
    private String goalType;

    @ColumnInfo(name = "is_completed")
    private boolean isCompleted;

    @ColumnInfo(name = "progress_percentage")
    private int progressPercentage;

    public Goal() {}

    @Ignore
    public Goal(String title, String description, double startWeight, double targetWeight, long targetDate, String goalType) {
        this.title = title;
        this.description = description;
        this.startWeight = startWeight;
        this.targetWeight = targetWeight;
        this.startDate = System.currentTimeMillis(); // Start is now
        this.targetDate = targetDate; // Target is future
        this.goalType = goalType;
        this.isCompleted = false;
        this.progressPercentage = 0;
    }

    // --- Logic: Calculate Progress ---
    public void updateProgress(double currentWeight) {
        if (startWeight == targetWeight) {
            this.progressPercentage = 100;
            return;
        }

        double totalToLose = Math.abs(startWeight - targetWeight);
        double lostSoFar = Math.abs(startWeight - currentWeight);

        // Prevent negative progress if weight moves in wrong direction
        if ((goalType.equals(TYPE_LOSE) && currentWeight > startWeight) ||
                (goalType.equals(TYPE_GAIN) && currentWeight < startWeight)) {
            this.progressPercentage = 0;
            return;
        }

        double pct = (lostSoFar / totalToLose) * 100.0;
        this.progressPercentage = (int) Math.min(100, Math.max(0, pct));

        if (this.progressPercentage >= 100) {
            this.isCompleted = true;
        }
    }

    // --- Getters & Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getStartWeight() { return startWeight; }
    public void setStartWeight(double startWeight) { this.startWeight = startWeight; }
    public double getTargetWeight() { return targetWeight; }
    public void setTargetWeight(double targetWeight) { this.targetWeight = targetWeight; }
    public long getStartDate() { return startDate; }
    public void setStartDate(long startDate) { this.startDate = startDate; }
    public long getTargetDate() { return targetDate; }
    public void setTargetDate(long targetDate) { this.targetDate = targetDate; }
    public String getGoalType() { return goalType; }
    public void setGoalType(String goalType) { this.goalType = goalType; }
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
    public int getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(int progressPercentage) { this.progressPercentage = progressPercentage; }
}