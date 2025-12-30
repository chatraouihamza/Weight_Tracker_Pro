package com.example.weighttrackerapp.utils;

import android.content.Context;

import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.weighttrackerapp.services.ReminderWorker;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class NotificationScheduler {

    public static void scheduleDailyReminders(Context context) {
        scheduleMorningReminder(context);
        scheduleEveningReminder(context);
    }

    private static void scheduleMorningReminder(Context context) {
        // Morning: 8:00 AM
        long delay = calculateInitialDelay(8, 0);

        Data inputData = new Data.Builder()
                .putString("title", "Good Morning! ☀️")
                .putString("message", "Don't forget to log your weight today.")
                .build();

        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(
                ReminderWorker.class, 24, TimeUnit.HOURS)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(inputData)
                .addTag("morning_reminder")
                .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "morning_work",
                ExistingPeriodicWorkPolicy.UPDATE,
                request
        );
    }

    private static void scheduleEveningReminder(Context context) {
        // Evening: 8:00 PM (20:00)
        long delay = calculateInitialDelay(20, 0);

        Data inputData = new Data.Builder()
                .putString("title", "Daily Wrap-up 🌙")
                .putString("message", "Did you track all your meals today?")
                .build();

        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(
                ReminderWorker.class, 24, TimeUnit.HOURS)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(inputData)
                .addTag("evening_reminder")
                .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "evening_work",
                ExistingPeriodicWorkPolicy.UPDATE,
                request
        );
    }

    private static long calculateInitialDelay(int targetHour, int targetMinute) {
        Calendar current = Calendar.getInstance();
        Calendar target = Calendar.getInstance();
        target.set(Calendar.HOUR_OF_DAY, targetHour);
        target.set(Calendar.MINUTE, targetMinute);
        target.set(Calendar.SECOND, 0);

        if (target.before(current)) {
            target.add(Calendar.DAY_OF_YEAR, 1);
        }

        return target.getTimeInMillis() - current.getTimeInMillis();
    }

    // Optional: Stop all if user disables notifications
    public static void cancelAll(Context context) {
        WorkManager.getInstance(context).cancelAllWork();
    }
}