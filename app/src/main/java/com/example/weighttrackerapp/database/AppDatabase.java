package com.example.weighttrackerapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.weighttrackerapp.models.ActivityEntry;
import com.example.weighttrackerapp.models.FoodEntry;
import com.example.weighttrackerapp.models.Goal;
import com.example.weighttrackerapp.models.Measurement;
import com.example.weighttrackerapp.models.UserProfile;
import com.example.weighttrackerapp.models.WeightEntry;
import com.example.weighttrackerapp.utils.DateConverter;

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
        ActivityEntry.class
    },
    version = 1,
    exportSchema = false
)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    
    private static volatile AppDatabase instance;
    
    public abstract UserProfileDao userProfileDao();
    public abstract WeightEntryDao weightEntryDao();
    public abstract MeasurementDao measurementDao();
    public abstract GoalDao goalDao();
    public abstract FoodEntryDao foodEntryDao();
    public abstract ActivityEntryDao activityEntryDao();
    
    /**
     * Get singleton instance of AppDatabase.
     */
    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "weight_tracker_db"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return instance;
    }
}
