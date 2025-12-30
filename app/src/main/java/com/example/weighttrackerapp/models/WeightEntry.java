package com.example.weighttrackerapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.io.Serializable;

/**
 * WeightEntry model representing a single weight measurement entry.
 */
@Entity(tableName = "weight_entries")
public class WeightEntry implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "user_id")
    private int userId;
    @ColumnInfo(name = "weight_kg")
    private double weight; // in kg

    // CHANGED: Date object -> long timestamp
    @ColumnInfo(name = "date_timestamp")
    private long date;

    private double bmi;

    @ColumnInfo(name = "body_fat_percentage")
    private double bodyFatPercentage;

    @ColumnInfo(name = "muscle_mass_percentage")
    private double muscleMassPercentage;

    // Empty Constructor for Room
    public WeightEntry() {
    }

    // Constructor 1: Basic
    @Ignore
    public WeightEntry(double weight, long date) {
        this.weight = weight;
        this.date = date;
    }

    // Constructor 2: Full (with body composition)
    @Ignore
    public WeightEntry(double weight, long date, double bmi,
                       double bodyFatPercentage, double muscleMassPercentage) {
        this.weight = weight;
        this.date = date;
        this.bmi = bmi;
        this.bodyFatPercentage = bodyFatPercentage;
        this.muscleMassPercentage = muscleMassPercentage;
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    // Returns timestamp (long)
    public long getDate() {
        return date;
    }

    // Expects timestamp (long)
    public void setDate(long date) {
        this.date = date;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public double getBodyFatPercentage() {
        return bodyFatPercentage;
    }

    public void setBodyFatPercentage(double bodyFatPercentage) {
        this.bodyFatPercentage = bodyFatPercentage;
    }

    public double getMuscleMassPercentage() {
        return muscleMassPercentage;
    }

    public void setMuscleMassPercentage(double muscleMassPercentage) {
        this.muscleMassPercentage = muscleMassPercentage;
    }

}