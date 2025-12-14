package com.example.weighttrackerapp.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.io.Serializable;
import java.util.Date;

/**
 * Measurement model representing body composition measurements.
 */
@Entity(tableName = "measurements")
public class Measurement implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private Date date;
    private double waist; // in cm
    private double hips; // in cm
    private double chest; // in cm
    private double muscleMass; // in percentage
    private double bodyFat; // in percentage
    private String notes;
    
    // Constructors
    public Measurement() {
    }
    
    @Ignore
    public Measurement(Date date, double waist, double hips, double chest, 
                      double muscleMass, double bodyFat, String notes) {
        this.date = date;
        this.waist = waist;
        this.hips = hips;
        this.chest = chest;
        this.muscleMass = muscleMass;
        this.bodyFat = bodyFat;
        this.notes = notes;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public double getWaist() {
        return waist;
    }
    
    public void setWaist(double waist) {
        this.waist = waist;
    }
    
    public double getHips() {
        return hips;
    }
    
    public void setHips(double hips) {
        this.hips = hips;
    }
    
    public double getChest() {
        return chest;
    }
    
    public void setChest(double chest) {
        this.chest = chest;
    }
    
    public double getMuscleMass() {
        return muscleMass;
    }
    
    public void setMuscleMass(double muscleMass) {
        this.muscleMass = muscleMass;
    }
    
    public double getBodyFat() {
        return bodyFat;
    }
    
    public void setBodyFat(double bodyFat) {
        this.bodyFat = bodyFat;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    @Override
    public String toString() {
        return "Measurement{" +
                "id=" + id +
                ", date=" + date +
                ", waist=" + waist +
                ", hips=" + hips +
                ", chest=" + chest +
                ", muscleMass=" + muscleMass +
                ", bodyFat=" + bodyFat +
                ", notes='" + notes + '\'' +
                '}';
    }
}
