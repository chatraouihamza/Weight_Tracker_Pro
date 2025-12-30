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

import java.util.Calendar;

public class WaterWorker extends Worker {

    private static final String CHANNEL_ID = "water_channel";

    public WaterWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // 1. Check Time Window (9 AM to 10 PM)
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);

        if (hour >= 9 && hour <= 22) {
            sendWaterNotification();
        }

        return Result.success();
    }

    private void sendWaterNotification() {
        Context context = getApplicationContext();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Hydration", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        // Action: Open App
        Intent openIntent = new Intent(context, MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(context, 101, openIntent, PendingIntent.FLAG_IMMUTABLE);

        // Action: Add Water Immediately (Broadcast)
        Intent addIntent = new Intent(context, WaterActionReceiver.class);
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 102, addIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_nav_food) // Use generic icon or add a water drop icon
                .setContentTitle("Hydration Check 💧")
                .setContentText("Time for a glass of water?")
                .setContentIntent(contentPendingIntent)
                .addAction(R.drawable.ic_input_add, "Add Cup (+250ml)", actionPendingIntent) // THE ACTION BUTTON
                .setAutoCancel(true);

        manager.notify(200, builder.build());
    }
}