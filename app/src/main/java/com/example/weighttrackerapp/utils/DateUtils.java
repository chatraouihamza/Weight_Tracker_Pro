package com.example.weighttrackerapp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    // Common formats
    public static final String FORMAT_DATE_FULL = "EEE, MMM d, yyyy"; // "Mon, Dec 20, 2025"
    public static final String FORMAT_TIME_SHORT = "HH:mm";           // "14:30"

    /**
     * Returns the timestamp (long) for 00:00:00:000 of the given date.
     */
    public static long getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * Returns the timestamp (long) for 23:59:59:999 of the given date.
     */
    public static long getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * Formats a timestamp into a readable string.
     */
    public static String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE_FULL, Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    /**
     * Formats a timestamp into just the time.
     */
    public static String formatTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_TIME_SHORT, Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}