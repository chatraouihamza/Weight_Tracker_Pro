package com.example.weighttrackerapp.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.io.Serializable;
import java.util.Date;

/**
 * FoodEntry model representing food/nutrition entries.
 */
@Entity(tableName = "food_entries")
public class FoodEntry implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String foodName;
    private Date date;
    private int calories;
    private double protein; // in grams
    private double carbs; // in grams
    private double fat; // in grams
    private String mealType; // BREAKFAST, LUNCH, DINNER, SNACK
    private double servingSize;
    private String servingUnit; // grams, ml, piece, etc.
    private String notes;
    
    // Constructors
    public FoodEntry() {
    }
    
    @Ignore
    public FoodEntry(String foodName, Date date, int calories, double protein, 
                    double carbs, double fat, String mealType) {
        this.foodName = foodName;
        this.date = date;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.mealType = mealType;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getFoodName() {
        return foodName;
    }
    
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public int getCalories() {
        return calories;
    }
    
    public void setCalories(int calories) {
        this.calories = calories;
    }
    
    public double getProtein() {
        return protein;
    }
    
    public void setProtein(double protein) {
        this.protein = protein;
    }
    
    public double getCarbs() {
        return carbs;
    }
    
    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }
    
    public double getFat() {
        return fat;
    }
    
    public void setFat(double fat) {
        this.fat = fat;
    }
    
    public String getMealType() {
        return mealType;
    }
    
    public void setMealType(String mealType) {
        this.mealType = mealType;
    }
    
    public double getServingSize() {
        return servingSize;
    }
    
    public void setServingSize(double servingSize) {
        this.servingSize = servingSize;
    }
    
    public String getServingUnit() {
        return servingUnit;
    }
    
    public void setServingUnit(String servingUnit) {
        this.servingUnit = servingUnit;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    @Override
    public String toString() {
        return "FoodEntry{" +
                "id=" + id +
                ", foodName='" + foodName + '\'' +
                ", date=" + date +
                ", calories=" + calories +
                ", protein=" + protein +
                ", carbs=" + carbs +
                ", fat=" + fat +
                ", mealType='" + mealType + '\'' +
                '}';
    }
}
