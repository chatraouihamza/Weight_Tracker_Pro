package com.example.weighttrackerapp.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.io.Serializable;
import java.util.Date;

/**
 * WeightEntry model representing a single weight measurement entry.
 */
@Entity(tableName = "weight_entries")
public class WeightEntry implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private double weight; // in kg
    private Date date;
    private String notes;
    private double bmi;
    private double bodyFatPercentage;
    private double muscleMassPercentage;
    
    // Constructors
    public WeightEntry() {
    }
    
    @Ignore
    public WeightEntry(double weight, Date date, String notes) {
        this.weight = weight;
        this.date = date;
        this.notes = notes;
    }
    
    @Ignore
    public WeightEntry(double weight, Date date, String notes, double bmi, 
                       double bodyFatPercentage, double muscleMassPercentage) {
        this.weight = weight;
        this.date = date;
        this.notes = notes;
        this.bmi = bmi;
        this.bodyFatPercentage = bodyFatPercentage;
        this.muscleMassPercentage = muscleMassPercentage;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public double getWeight() {
        return weight;
    }
    
    public void setWeight(double weight) {
        this.weight = weight;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
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
    
    @Override
    public String toString() {
        return "WeightEntry{" +
                "id=" + id +
                ", weight=" + weight +
                ", date=" + date +
                ", notes='" + notes + '\'' +
                ", bmi=" + bmi +
                ", bodyFatPercentage=" + bodyFatPercentage +
                ", muscleMassPercentage=" + muscleMassPercentage +
                '}';
    }
}
