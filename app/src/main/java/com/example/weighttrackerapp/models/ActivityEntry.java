package com.example.weighttrackerapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "activity_entries")
public class ActivityEntry implements Serializable {

    // --- Constants for Type Safety ---
    public static final String INTENSITY_LOW = "Low";
    public static final String INTENSITY_MEDIUM = "Medium";
    public static final String INTENSITY_HIGH = "High";

    public static final String TYPE_RUNNING = "Running";
    public static final String TYPE_WALKING = "Walking";
    public static final String TYPE_GYM = "Gym";
    public static final String TYPE_CYCLING = "Cycling";
    public static final String TYPE_SWIMMING = "Swimming";

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId;
    @ColumnInfo(name = "activity_type")
    private String activityType;

    @ColumnInfo(name = "date_timestamp")
    private long date;

    private int duration; // in minutes

    @ColumnInfo(name = "calories_burned")
    private int caloriesBurned;

    private String intensity; // Use Constants

    @ColumnInfo(name = "heart_rate")
    private int heartRate; // average bpm

    // Empty Constructor
    public ActivityEntry() {}

    @Ignore
    public ActivityEntry(String activityType, long date, int duration,
                         int caloriesBurned, String intensity) {
        this.activityType = activityType;
        this.date = date;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
        this.intensity = intensity;
    }

    // --- Getters and Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getActivityType() { return activityType; }
    public void setActivityType(String activityType) { this.activityType = activityType; }

    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public int getCaloriesBurned() { return caloriesBurned; }
    public void setCaloriesBurned(int caloriesBurned) { this.caloriesBurned = caloriesBurned; }

    public String getIntensity() { return intensity; }
    public void setIntensity(String intensity) { this.intensity = intensity; }

    public int getHeartRate() { return heartRate; }
    public void setHeartRate(int heartRate) { this.heartRate = heartRate; }
}