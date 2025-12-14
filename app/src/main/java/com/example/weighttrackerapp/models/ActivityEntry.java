package com.example.weighttrackerapp.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.io.Serializable;
import java.util.Date;

/**
 * ActivityEntry model representing physical activity entries.
 */
@Entity(tableName = "activity_entries")
public class ActivityEntry implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String activityType; // RUNNING, CYCLING, SWIMMING, GYM, YOGA, etc.
    private Date date;
    private int duration; // in minutes
    private int caloriesBurned;
    private String intensity; // LOW, MEDIUM, HIGH
    private double distance; // in km
    private String notes;
    private int heartRate; // average heart rate
    
    // Constructors
    public ActivityEntry() {
    }
    
    @Ignore
    public ActivityEntry(String activityType, Date date, int duration, 
                        int caloriesBurned, String intensity) {
        this.activityType = activityType;
        this.date = date;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
        this.intensity = intensity;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getActivityType() {
        return activityType;
    }
    
    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public int getCaloriesBurned() {
        return caloriesBurned;
    }
    
    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }
    
    public String getIntensity() {
        return intensity;
    }
    
    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }
    
    public double getDistance() {
        return distance;
    }
    
    public void setDistance(double distance) {
        this.distance = distance;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public int getHeartRate() {
        return heartRate;
    }
    
    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }
    
    @Override
    public String toString() {
        return "ActivityEntry{" +
                "id=" + id +
                ", activityType='" + activityType + '\'' +
                ", date=" + date +
                ", duration=" + duration +
                ", caloriesBurned=" + caloriesBurned +
                ", intensity='" + intensity + '\'' +
                '}';
    }
}
