package com.example.weighttrackerapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.annotation.NonNull;

import com.example.weighttrackerapp.models.ActivityEntry;
import com.example.weighttrackerapp.models.FoodEntry;
import com.example.weighttrackerapp.models.Goal;
import com.example.weighttrackerapp.models.Measurement;
import com.example.weighttrackerapp.models.UserProfile;
import com.example.weighttrackerapp.models.WeightEntry;
import com.example.weighttrackerapp.models.FoodItem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Room Database for Weight Tracker Pro application.
 * Manages all database operations and provides DAOs.
 */

@Database(
    entities = {
        UserProfile.class,
        WeightEntry.class,
        Measurement.class,
        Goal.class,
        FoodEntry.class,
        FoodItem.class,
        ActivityEntry.class
    },
    version = 3,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    
    private static volatile AppDatabase instance;
    
    public abstract UserProfileDao userProfileDao();
    public abstract WeightEntryDao weightEntryDao();
    public abstract MeasurementDao measurementDao();
    public abstract GoalDao goalDao();
    public abstract FoodEntryDao foodEntryDao();
    public abstract ActivityEntryDao activityEntryDao();
    public abstract FoodItemDao foodItemDao();

    private static final int NUMBER_OF_THREADS = 4;

    /**
     * Get singleton instance of AppDatabase.
     */
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "weight_tracker_db")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback) // Add Callback here
                            .build();
                }
            }
        }
        return instance;
    }

    // Callback to Pre-populate Data
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                FoodItemDao dao = instance.foodItemDao();
                dao.deleteAll();

                // Basic Staples (Per 100g)
                dao.insert(new FoodItem("Apple", 52, 0.3, 14, 0.2));
                dao.insert(new FoodItem("Banana", 89, 1.1, 23, 0.3));
                dao.insert(new FoodItem("Chicken Breast (Cooked)", 165, 31, 0, 3.6));
                dao.insert(new FoodItem("Rice (White, Cooked)", 130, 2.7, 28, 0.3));
                dao.insert(new FoodItem("Egg (Boiled)", 155, 13, 1.1, 11));
                dao.insert(new FoodItem("Oatmeal", 68, 2.4, 12, 1.4));
                dao.insert(new FoodItem("Broccoli", 34, 2.8, 7, 0.4));
                dao.insert(new FoodItem("Almonds", 579, 21, 22, 50));
                dao.insert(new FoodItem("Greek Yogurt", 59, 10, 3.6, 0.4));
                dao.insert(new FoodItem("Salmon", 208, 20, 0, 13));
                dao.insert(new FoodItem("Potato (Boiled)", 87, 1.9, 20, 0.1));
                dao.insert(new FoodItem("Bread (Whole Wheat)", 247, 13, 41, 3.4));
            });
        }
    };
}
