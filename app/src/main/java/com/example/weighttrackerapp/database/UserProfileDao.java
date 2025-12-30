package com.example.weighttrackerapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.OnConflictStrategy; // Import this


import com.example.weighttrackerapp.models.UserProfile;

@Dao
public interface UserProfileDao {

    // CHANGE: Added onConflict = REPLACE.
    // If ID 1 exists, it overwrites it. If not, it creates it.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(UserProfile userProfile);

    // Login Query: Returns the user if email/pass match, null otherwise
    @Query("SELECT * FROM user_profile WHERE email = :email AND password = :password LIMIT 1")
    UserProfile login(String email, String password);

    // Check if email already exists (for Registration)
    @Query("SELECT COUNT(*) FROM user_profile WHERE email = :email")
    int checkEmailExists(String email);

    @Update
    void update(UserProfile userProfile);

    @Query("SELECT * FROM user_profile WHERE id = :userId LIMIT 1")
    LiveData<UserProfile> getUserProfile(int userId);

    @Query("SELECT * FROM user_profile WHERE id = :userId LIMIT 1")
    UserProfile getUserProfileSync(int userId);

    @Query("DELETE FROM user_profile")
    void deleteAll();

    // Check if profile exists (returns 1 if exists, 0 if not)
    @Query("SELECT COUNT(*) FROM user_profile WHERE id = :userId")
    LiveData<Integer> hasProfile(int userId);
}
