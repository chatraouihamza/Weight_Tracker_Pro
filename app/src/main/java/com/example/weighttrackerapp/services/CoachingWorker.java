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
import com.example.weighttrackerapp.database.AppDatabase;
import com.example.weighttrackerapp.models.Goal;
import com.example.weighttrackerapp.utils.SessionManager;

import java.util.List;
import java.util.Random;

public class CoachingWorker extends Worker {

    private static final String CHANNEL_ID = "coaching_channel";

    public CoachingWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        SessionManager session = new SessionManager(context);
        int userId = session.getUserId();

        // 1. Get Goal Type from DB
        AppDatabase db = AppDatabase.getInstance(context);
        List<Goal> goals = db.goalDao().getActiveGoalsSync(userId); // Use the Synchronous method we made!

        if (goals != null && !goals.isEmpty()) {
            Goal activeGoal = goals.get(0);
            sendCoachingNotification(activeGoal.getGoalType());
        } else {
            sendCoachingNotification("MAINTAIN"); // Default
        }

        return Result.success();
    }

    private void sendCoachingNotification(String goalType) {
        Context context = getApplicationContext();
        String title = "Daily Coach 🏋️";
        String message = "";

        // 2. Logic based on Goal
        if (Goal.TYPE_LOSE.equals(goalType)) {
            String[] tips = {
                    "Time to move! How about a 15-minute brisk walk?",
                    "Burning calories is key. Have you exercised today?",
                    "Keep moving! Your goal is getting closer."
            };
            message = tips[new Random().nextInt(tips.length)];
        } else if (Goal.TYPE_GAIN.equals(goalType)) {
            String[] tips = {
                    "Fuel up! Have you had your protein snack?",
                    "Gains require fuel. Eat 50g of nuts or yogurt now.",
                    "Don't skip meals! Consistency builds muscle."
            };
            message = tips[new Random().nextInt(tips.length)];
        } else {
            message = "Stay consistent! Log your meals to stay on track.";
        }

        // 3. Send Notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Coaching", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 103, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_nav_goals)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        manager.notify(300, builder.build());
    }
}