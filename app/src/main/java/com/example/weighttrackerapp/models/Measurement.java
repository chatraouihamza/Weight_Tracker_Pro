package com.example.weighttrackerapp.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.io.Serializable;
import androidx.room.ColumnInfo;


/**
 * Measurement model representing body composition measurements.
 */



@Entity(tableName = "measurements")
public class Measurement implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId;

    // CHANGED: Use long for consistency with WeightEntry/FoodEntry
    @ColumnInfo(name = "date_timestamp")
    private long date;

    @ColumnInfo(name = "waist_cm")
    private double waist;

    @ColumnInfo(name = "hips_cm")
    private double hips;

    @ColumnInfo(name = "chest_cm")
    private double chest;

    @ColumnInfo(name = "muscle_mass_pct")
    private double muscleMass;

    @ColumnInfo(name = "body_fat_pct")
    private double bodyFat;

    private String notes;

    // Empty Constructor
    public Measurement() {}

    @Ignore
    public Measurement(long date, double waist, double hips, double chest,
                       double muscleMass, double bodyFat, String notes) {
        this.date = date;
        this.waist = waist;
        this.hips = hips;
        this.chest = chest;
        this.muscleMass = muscleMass;
        this.bodyFat = bodyFat;
        this.notes = notes;
    }

    // --- Helper Logic: Waist-to-Hip Ratio (WHR) ---
    // WHR is a key health indicator
    public double getWaistToHipRatio() {
        if (hips == 0) return 0;
        return waist / hips;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }

    public double getWaist() { return waist; }
    public void setWaist(double waist) { this.waist = waist; }

    public double getHips() { return hips; }
    public void setHips(double hips) { this.hips = hips; }

    public double getChest() { return chest; }
    public void setChest(double chest) { this.chest = chest; }

    public double getMuscleMass() { return muscleMass; }
    public void setMuscleMass(double muscleMass) { this.muscleMass = muscleMass; }

    public double getBodyFat() { return bodyFat; }
    public void setBodyFat(double bodyFat) { this.bodyFat = bodyFat; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

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
