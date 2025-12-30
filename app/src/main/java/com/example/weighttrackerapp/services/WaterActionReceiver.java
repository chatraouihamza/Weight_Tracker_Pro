package com.example.weighttrackerapp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.weighttrackerapp.utils.DateUtils;

public class WaterActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 1. Get Shared Preferences (where we store water count)
        SharedPreferences prefs = context.getSharedPreferences("daily_tracker", Context.MODE_PRIVATE);

        // 2. Generate Today's Key
        String key = "water_" + DateUtils.formatDate(System.currentTimeMillis());

        // 3. Increment Count
        int current = prefs.getInt(key, 0);
        int newVal = current + 1;

        prefs.edit().putInt(key, newVal).apply();

        // 4. Feedback to User
        Toast.makeText(context, "Water added! Total: " + newVal + " glasses", Toast.LENGTH_SHORT).show();

        // Optional: Dismiss the notification (requires passing notification ID)
        // NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID);
    }
}