package com.example.weighttrackerapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.weighttrackerapp.models.UserProfile;

/**
 * Data Access Object (DAO) for UserProfile entities.
 */
@Dao
public interface UserProfileDao {
    
    @Insert
    void insert(UserProfile userProfile);
    
    @Update
    void update(UserProfile userProfile);
    
    @Query("SELECT * FROM user_profile WHERE id = 1")
    LiveData<UserProfile> getUserProfile();
    
    @Query("SELECT * FROM user_profile WHERE id = 1")
    UserProfile getUserProfileSync();
    
    @Query("DELETE FROM user_profile")
    void deleteAll();
}
