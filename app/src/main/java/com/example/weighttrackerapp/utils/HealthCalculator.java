package com.example.weighttrackerapp.utils;

import com.example.weighttrackerapp.models.UserProfile;

import java.util.concurrent.TimeUnit;

public class HealthCalculator {

    // --- Constants ---
    private static final int CALORIES_PER_KG_FAT = 7700;
    private static final int MIN_SAFE_CALORIES_MALE = 1500;
    private static final int MIN_SAFE_CALORIES_FEMALE = 1200;

    /**
     * Simple container for Macro results
     */
    public static class MacroNutrients {
        public int calories;
        public int protein;
        public int carbs;
        public int fat;
        public int waterMl;

        public MacroNutrients(int calories, int protein, int carbs, int fat, int waterMl) {
            this.calories = calories;
            this.protein = protein;
            this.carbs = carbs;
            this.fat = fat;
            this.waterMl = waterMl;
        }
    }

    /**
     * MASTER CALCULATION METHOD
     * Calculates everything based on User Profile and Goal.
     */
    public static MacroNutrients calculateNeeds(UserProfile user, double currentWeight,
                                                double targetWeight, long targetDate) {

        // 1. Calculate BMR (Mifflin-St Jeor)
        double bmr = calculateBMR(currentWeight, user.getHeight(), user.getAge(), user.getGender());

        // 2. Calculate Maintenance Calories (TDEE)
        double activityMultiplier = getTDEEMultiplier(user.getActivityLevel());
        double tdee = bmr * activityMultiplier;

        // 3. Calculate Daily Target based on Goal Deadline
        int dailyCalories = calculateGoalCalories(tdee, currentWeight, targetWeight, targetDate, user.getGender());

        // 4. Calculate Macros (Standard Balanced Diet)
        // Protein: 30%, Fat: 30%, Carbs: 40%
        int proteinGrams = (int) ((dailyCalories * 0.30) / 4);
        int fatGrams = (int) ((dailyCalories * 0.30) / 9);
        int carbsGrams = (int) ((dailyCalories * 0.40) / 4);

        // 5. Calculate Water (35ml per kg is a standard health baseline)
        int waterMl = (int) (currentWeight * 35);
        // Add extra for high activity
        if (activityMultiplier > 1.5) waterMl += 500;

        return new MacroNutrients(dailyCalories, proteinGrams, carbsGrams, fatGrams, waterMl);
    }

    // --- Internal Math Methods ---

    private static double calculateBMR(double weight, double height, int age, String gender) {
        double s = 5;
        if (UserProfile.GENDER_FEMALE.equalsIgnoreCase(gender)) {
            s = -161;
        }
        // Formula: 10*W + 6.25*H - 5*A + S
        return (10 * weight) + (6.25 * height) - (5 * age) + s;
    }

    private static double getTDEEMultiplier(String activityLevel) {
        if (activityLevel == null) return 1.2;
        switch (activityLevel) {
            case UserProfile.LEVEL_LIGHT: return 1.375;
            case UserProfile.LEVEL_MODERATE: return 1.55;
            case UserProfile.LEVEL_ACTIVE: return 1.725;
            default: return 1.2; // Sedentary
        }
    }

    private static int calculateGoalCalories(double tdee, double currentWeight,
                                             double targetWeight, long targetDate, String gender) {

        // If no goal set or maintaining
        if (targetWeight == 0 || currentWeight == targetWeight) {
            return (int) tdee;
        }

        // 1. Calculate Total Calorie Gap
        // e.g., Lose 2kg = -15,400 kcal total needed
        double weightDifference = targetWeight - currentWeight;
        double totalCaloriesNeeded = weightDifference * CALORIES_PER_KG_FAT;

        // 2. Calculate Days Remaining
        long diffInMillis = targetDate - System.currentTimeMillis();
        long daysRemaining = TimeUnit.MILLISECONDS.toDays(diffInMillis);

        if (daysRemaining <= 0) daysRemaining = 1; // Avoid division by zero

        // 3. Calculate Daily Deficit/Surplus
        double dailyAdjustment = totalCaloriesNeeded / daysRemaining;

        // 4. Apply to TDEE
        double targetCalories = tdee + dailyAdjustment;

        // 5. SAFETY CHECKS (Crucial for Health App)
        // Don't starve the user. Cap the deficit.
        int minSafe = UserProfile.GENDER_MALE.equalsIgnoreCase(gender) ?
                MIN_SAFE_CALORIES_MALE : MIN_SAFE_CALORIES_FEMALE;

        if (targetCalories < minSafe) {
            return minSafe; // Return minimum safe limit
        }

        // Also don't suggest eating 5000+ calories unless they are an athlete
        if (targetCalories > 4000) {
            return 4000;
        }

        return (int) targetCalories;
    }

    // Helper for BMI Category
    public static double calculateBMI(double weightKg, double heightCm) {
        if (heightCm <= 0) return 0;
        double heightM = heightCm / 100.0;
        return weightKg / (heightM * heightM);
    }

    public static double calculateWHR(double waist, double hips) {
        if (hips == 0) return 0;
        return waist / hips;
    }

    public static double calculateBodyFat(double waist, double neck, double height, double hips, String gender) {
        // 1. Basic Validation
        if (waist <= 0 || height <= 0 || neck <= 0) return 0;

        double bodyFat = 0;

        try {
            // 2. Case-Insensitive Check
            // Ensure gender is not null
            if (gender != null && gender.equalsIgnoreCase("Male")) {
                // --- MALE FORMULA ---
                // Restriction: Waist must be larger than Neck
                if ((waist - neck) <= 0) return 0;

                double logWaistNeck = Math.log10(waist - neck);
                double logHeight = Math.log10(height);

                // Formula: 495 / (1.0324 - 0.19077(log(W-N)) + 0.15456(log(H))) - 450
                bodyFat = 495 / (1.0324 - 0.19077 * logWaistNeck + 0.15456 * logHeight) - 450;

            } else {
                // --- FEMALE FORMULA ---
                // Default to Female if gender is null or anything else
                if (hips <= 0) return 0;

                // Restriction: Waist + Hips must be larger than Neck
                if ((waist + hips - neck) <= 0) return 0;

                double logWaistHipsNeck = Math.log10(waist + hips - neck);
                double logHeight = Math.log10(height);

                // Formula: 495 / (1.29579 - 0.35004(log(W+H-N)) + 0.22100(log(H))) - 450
                bodyFat = 495 / (1.29579 - 0.35004 * logWaistHipsNeck + 0.22100 * logHeight) - 450;
            }
        } catch (Exception e) {
            return 0; // Math error
        }

        // 3. Sanity Check (Cap results between 1% and 70%)
        if (bodyFat < 1) return 1;
        if (bodyFat > 70) return 70;

        return bodyFat;
    }

    /**
     * Estimate Muscle Mass %.
     * Scientific Estimation: Muscle is roughly 40-50% of weight, or ~60% of Lean Body Mass.
     * Formula: (100 - BodyFat) * 0.6
     */
    public static double calculateMuscleMassPercentage(double bodyFatPercentage) {
        if (bodyFatPercentage <= 0 || bodyFatPercentage >= 100) return 0;
        double leanMassPercentage = 100 - bodyFatPercentage;
        return leanMassPercentage * 0.6; // Estimation factor
    }

    public static String getBMICategory(double bmi) {
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25) return "Normal";
        if (bmi < 30) return "Overweight";
        return "Obese";
    }
}