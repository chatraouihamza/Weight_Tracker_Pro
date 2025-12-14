package com.example.weighttrackerapp.utils;

/**
 * Utility class for health-related calculations.
 */
public class HealthCalculator {
    
    /**
     * Calculate BMI (Body Mass Index).
     * Formula: weight (kg) / (height (m) ^ 2)
     */
    public static double calculateBMI(double weightKg, double heightCm) {
        if (heightCm <= 0) return 0;
        double heightM = heightCm / 100.0;
        return weightKg / (heightM * heightM);
    }
    
    /**
     * Get BMI category based on BMI value.
     */
    public static String getBMICategory(double bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 25) {
            return "Normal weight";
        } else if (bmi < 30) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }
    
    /**
     * Calculate daily calorie needs using Mifflin-St Jeor equation.
     * Returns BMR (Basal Metabolic Rate).
     */
    public static double calculateBMR(double weightKg, double heightCm, int age, String gender) {
        if ("MALE".equalsIgnoreCase(gender)) {
            return 10 * weightKg + 6.25 * heightCm - 5 * age + 5;
        } else if ("FEMALE".equalsIgnoreCase(gender)) {
            return 10 * weightKg + 6.25 * heightCm - 5 * age - 161;
        }
        return 0;
    }
    
    /**
     * Calculate TDEE (Total Daily Energy Expenditure) based on activity level.
     * activityFactor: 1.2 (sedentary), 1.375 (light), 1.55 (moderate), 1.725 (very active), 1.9 (extremely active)
     */
    public static double calculateTDEE(double bmr, double activityFactor) {
        return bmr * activityFactor;
    }
    
    /**
     * Calculate ideal weight range based on height using BMI formula.
     * Returns array [minWeight, maxWeight] for BMI 18.5-24.9 (normal range)
     */
    public static double[] getIdealWeightRange(double heightCm) {
        double heightM = heightCm / 100.0;
        double minWeight = 18.5 * (heightM * heightM);
        double maxWeight = 24.9 * (heightM * heightM);
        return new double[]{minWeight, maxWeight};
    }
    
    /**
     * Calculate weight change needed to reach target weight.
     */
    public static double calculateWeightChange(double currentWeight, double targetWeight) {
        return targetWeight - currentWeight;
    }
    
    /**
     * Calculate percentage of weight change.
     */
    public static double calculateWeightChangePercentage(double startWeight, double currentWeight) {
        if (startWeight == 0) return 0;
        return ((currentWeight - startWeight) / startWeight) * 100;
    }
    
    /**
     * Calculate water intake recommendation in liters.
     * Formula: weight (kg) * 0.033 liters
     */
    public static double calculateWaterIntake(double weightKg) {
        return weightKg * 0.033;
    }
    
    /**
     * Calculate macro nutrients distribution.
     * Returns array [protein, carbs, fat] in grams based on calorie target.
     * Default: 30% protein, 40% carbs, 30% fat
     */
    public static double[] calculateMacroNutrients(int calorieTarget) {
        double protein = (calorieTarget * 0.30) / 4; // 4 cal per gram
        double carbs = (calorieTarget * 0.40) / 4;   // 4 cal per gram
        double fat = (calorieTarget * 0.30) / 9;     // 9 cal per gram
        return new double[]{protein, carbs, fat};
    }
}
