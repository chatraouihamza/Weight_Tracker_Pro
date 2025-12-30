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

    @ColumnInfo(name = "neck_cm")
    private double neck;

    // Empty Constructor
    public Measurement() {}

    @Ignore
    public Measurement(long date, double waist, double hips, double chest, double neck) {
        this.date = date;
        this.waist = waist;
        this.hips = hips;
        this.chest = chest;
        this.neck = neck;
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

    public double getNeck() { return neck; }
    public void setNeck(double neck) { this.neck = neck; }


}
