package com.example.weighttrackerapp.models;

import java.util.ArrayList;
import java.util.List;

public class MealGroup {
    private String mealType; // "Breakfast"
    private int totalCalories;
    private List<FoodEntry> foods;

    public MealGroup(String mealType) {
        this.mealType = mealType;
        this.foods = new ArrayList<>();
        this.totalCalories = 0;
    }

    public void addEntry(FoodEntry entry) {
        foods.add(entry);
        totalCalories += entry.getCalories();
    }

    public String getMealType() { return mealType; }
    public int getTotalCalories() { return totalCalories; }
    public List<FoodEntry> getFoods() { return foods; }

    // Generates string: "Apple + Banana + Milk"
    public String getFoodDescription() {
        if (foods.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < foods.size(); i++) {
            sb.append(foods.get(i).getFoodName());
            if (i < foods.size() - 1) sb.append(" + ");
        }
        return sb.toString();
    }
}