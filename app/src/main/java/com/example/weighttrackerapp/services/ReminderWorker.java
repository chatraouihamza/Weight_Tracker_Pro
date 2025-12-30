package com.example.weighttrackerapp.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.weighttrackerapp.R;
import com.example.weighttrackerapp.activities.MainActivity;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.weighttrackerapp.services.CoachingWorker;
import com.example.weighttrackerapp.services.WaterWorker;

import java.util.concurrent.TimeUnit;

public class ReminderWorker extends Worker {

    private static final String CHANNEL_ID = "weight_tracker_channel";

    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // 1. Get Input Data (Title/Message passed when scheduling)
        String title = getInputData().getString("title");
        String message = getInputData().getString("message");

        if (title == null) title = "Weight Tracker Pro";
        if (message == null) message = "Time to log your progress!";

        // 2. Trigger Notification
        showNotification(title, message);

        return Result.success();
    }

    private void showNotification(String title, String message) {
        Context context = getApplicationContext();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        WorkManager workManager = WorkManager.getInstance(context);


        // Create Channel (Required for Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Daily Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            manager.createNotificationChannel(channel);
        }

        // Create Intent (What happens when clicked)
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        );

        // Build Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_nav_weight) // Use one of your existing vector icons
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        manager.notify(1, builder.build());

        // 1. Water Reminder (Every 3 hours)
        // The worker itself checks if time is 9am-10pm
        PeriodicWorkRequest waterRequest = new PeriodicWorkRequest.Builder(
                WaterWorker.class, 3, TimeUnit.HOURS)
                .addTag("water_work")
                .build();

        workManager.enqueueUniquePeriodicWork(
                "water_periodic",
                ExistingPeriodicWorkPolicy.UPDATE,
                waterRequest
        );

        // 2. Coaching Reminder (Every 6 hours)
        PeriodicWorkRequest coachingRequest = new PeriodicWorkRequest.Builder(
                CoachingWorker.class, 6, TimeUnit.HOURS)
                .addTag("coaching_work")
                .build();

        workManager.enqueueUniquePeriodicWork(
                "coaching_periodic",
                ExistingPeriodicWorkPolicy.UPDATE,
                coachingRequest
        );
    }
    public static void cancelAll(Context context) {
        WorkManager.getInstance(context).cancelAllWork();
    }
}