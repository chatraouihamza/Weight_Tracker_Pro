package com.example.weighttrackerapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class for formatting and conversion operations.
 */
public class FormatUtils {
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    private static final SimpleDateFormat chartDateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());


    /**
     * Format timestamp to string (dd/MM/yyyy).
     */
    public static String formatDate(long timestamp) {
        return dateFormat.format(new Date(timestamp));
    }

    /**
     * Format timestamp to string (HH:mm).
     */
    public static String formatTime(long timestamp) {
        return timeFormat.format(new Date(timestamp));
    }

    /**
     * Format timestamp to string (dd/MM/yyyy HH:mm).
     */
    public static String formatDateTime(long timestamp) {
        return dateTimeFormat.format(new Date(timestamp));
    }

    /**
     * Format date to string (dd/MM/yyyy).
     */
    public static String formatDate(Date date) {
        return date != null ? dateFormat.format(date) : "";
    }

    /**
     * Format timestamp (long) for chart display (dd MMM).
     */
    public static String formatChartDate(long timestamp) {
        return chartDateFormat.format(new Date(timestamp));
    }
    /**
     * Format weight value with 2 decimal places.
     */
    public static String formatWeight(double weight) {
        return String.format(Locale.getDefault(), "%.2f", weight);
    }
    
    /**
     * Format BMI value with 1 decimal place.
     */
    public static String formatBMI(double bmi) {
        return String.format(Locale.getDefault(), "%.1f", bmi);
    }
    
    /**
     * Format calories as integer.
     */
    public static String formatCalories(int calories) {
        return String.format(Locale.getDefault(), "%d", calories);
    }
    
    /**
     * Format macronutrient values with 1 decimal place.
     */
    public static String formatMacro(double value) {
        return String.format(Locale.getDefault(), "%.1f", value);
    }
    
    /**
     * Format percentage value with 1 decimal place.
     */
    public static String formatPercentage(double percentage) {
        return String.format(Locale.getDefault(), "%.1f%%", percentage);
    }
    
    /**
     * Convert kg to lbs.
     */
    public static double kgToLbs(double kg) {
        return kg * 2.20462;
    }
    
    /**
     * Convert lbs to kg.
     */
    public static double lbsToKg(double lbs) {
        return lbs / 2.20462;
    }
    
    /**
     * Convert cm to inches.
     */
    public static double cmToInches(double cm) {
        return cm / 2.54;
    }
    
    /**
     * Convert inches to cm.
     */
    public static double inchesToCm(double inches) {
        return inches * 2.54;
    }
    
    /**
     * Format duration in minutes to readable format (e.g., "1h 30m").
     */
    public static String formatDuration(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        
        if (hours == 0) {
            return mins + "m";
        } else if (mins == 0) {
            return hours + "h";
        } else {
            return hours + "h " + mins + "m";
        }
    }
    
    /**
     * Format distance with 2 decimal places and unit.
     */
    public static String formatDistance(double distance) {
        return String.format(Locale.getDefault(), "%.2f km", distance);
    }
}
