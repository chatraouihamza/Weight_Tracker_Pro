package com.example.weighttrackerapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "food_entries")
public class FoodEntry implements Serializable {

    // Constants
    public static final String MEAL_BREAKFAST = "Breakfast";
    public static final String MEAL_LUNCH = "Lunch";
    public static final String MEAL_DINNER = "Dinner";
    public static final String MEAL_SNACK = "Snack";

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "user_id")
    private int userId;
    @ColumnInfo(name = "food_name")
    private String foodName;

    // Store as timestamp for sorting
    @ColumnInfo(name = "date_timestamp")
    private long date;

    private int calories;
    private double protein;
    private double carbs;
    private double fat;

    @ColumnInfo(name = "meal_type")
    private String mealType;

    // Constructors
    public FoodEntry() {}

    @Ignore
    public FoodEntry(String foodName, long date, int calories, double protein,
                     double carbs, double fat, String mealType) {
        this.foodName = foodName;
        this.date = date;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.mealType = mealType;
    }

    // Getters/Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }
    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }
    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }
    public double getProtein() { return protein; }
    public void setProtein(double protein) { this.protein = protein; }
    public double getCarbs() { return carbs; }
    public void setCarbs(double carbs) { this.carbs = carbs; }
    public double getFat() { return fat; }
    public void setFat(double fat) { this.fat = fat; }
    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }
}