package com.example.weighttrackerapp.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.io.Serializable;
import java.util.Date;

/**
 * Goal model representing health and fitness goals.
 */
@Entity(tableName = "goals")
public class Goal implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String title;
    private String description;
    private double targetWeight; // in kg
    private double startWeight; // in kg
    private Date startDate;
    private Date targetDate;
    private String goalType; // LOSE_WEIGHT, GAIN_WEIGHT, MAINTAIN
    private boolean isCompleted;
    private Date completedDate;
    private int progressPercentage;
    
    // Constructors
    public Goal() {
    }
    
    @Ignore
    public Goal(String title, String description, double targetWeight, 
                double startWeight, Date startDate, Date targetDate, String goalType) {
        this.title = title;
        this.description = description;
        this.targetWeight = targetWeight;
        this.startWeight = startWeight;
        this.startDate = startDate;
        this.targetDate = targetDate;
        this.goalType = goalType;
        this.isCompleted = false;
        this.progressPercentage = 0;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public double getTargetWeight() {
        return targetWeight;
    }
    
    public void setTargetWeight(double targetWeight) {
        this.targetWeight = targetWeight;
    }
    
    public double getStartWeight() {
        return startWeight;
    }
    
    public void setStartWeight(double startWeight) {
        this.startWeight = startWeight;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getTargetDate() {
        return targetDate;
    }
    
    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }
    
    public String getGoalType() {
        return goalType;
    }
    
    public void setGoalType(String goalType) {
        this.goalType = goalType;
    }
    
    public boolean isCompleted() {
        return isCompleted;
    }
    
    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
    
    public Date getCompletedDate() {
        return completedDate;
    }
    
    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }
    
    public int getProgressPercentage() {
        return progressPercentage;
    }
    
    public void setProgressPercentage(int progressPercentage) {
        this.progressPercentage = progressPercentage;
    }
    
    @Override
    public String toString() {
        return "Goal{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", targetWeight=" + targetWeight +
                ", goalType='" + goalType + '\'' +
                ", isCompleted=" + isCompleted +
                ", progressPercentage=" + progressPercentage +
                '}';
    }
}
