package com.example.weighttrackerapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "user_profile")
public class UserProfile implements Serializable {

    // --- Constants ---
    public static final String GENDER_MALE = "Male";
    public static final String GENDER_FEMALE = "Female";
    public static final String UNIT_KG = "KG";
    public static final String UNIT_LBS = "LBS";

    public static final String LEVEL_SEDENTARY = "Sedentary";
    public static final String LEVEL_LIGHT = "Light";
    public static final String LEVEL_MODERATE = "Moderate";
    public static final String LEVEL_ACTIVE = "Active";

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String email;
    private String password;
    private int age;
    private String gender;
    private double height;
    private String unitPreference;

    @ColumnInfo(name = "activity_level")
    private String activityLevel;

    @ColumnInfo(name = "initial_weight")
    private double initialWeight;

    @ColumnInfo(name = "metabolism_type")
    private String metabolismType; // "Slow", "Average", "Fast"

    // CHANGED: Date -> long
    @ColumnInfo(name = "created_date")
    private long createdDate;

    // CHANGED: Date -> long
    @ColumnInfo(name = "last_updated_date")
    private long lastUpdatedDate;

    private String profilePhotoUrl;
    private boolean notificationsEnabled;
    private String notificationTime;

    public UserProfile() {}

    @Ignore
    public UserProfile(String name, String email, String password, int age,
                       String gender, double height, double weight,
                       String activityLevel, String metabolismType) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.initialWeight = weight; // Set weight
        this.activityLevel = activityLevel;
        this.metabolismType = metabolismType; // Set type
        this.createdDate = System.currentTimeMillis();
        this.lastUpdatedDate = System.currentTimeMillis();
        this.unitPreference = UNIT_KG;
        this.notificationsEnabled = true;
        this.notificationTime = "08:00";
    }

    // --- Getters & Setters ---
    // Ensure these use LONG, not Date
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public String getUnitPreference() { return unitPreference; }
    public void setUnitPreference(String unitPreference) { this.unitPreference = unitPreference; }

    public String getActivityLevel() { return activityLevel; }
    public void setActivityLevel(String activityLevel) { this.activityLevel = activityLevel; }

    public long getCreatedDate() { return createdDate; }
    public void setCreatedDate(long createdDate) { this.createdDate = createdDate; }

    public long getLastUpdatedDate() { return lastUpdatedDate; }
    public void setLastUpdatedDate(long lastUpdatedDate) { this.lastUpdatedDate = lastUpdatedDate; }

    public String getProfilePhotoUrl() { return profilePhotoUrl; }
    public void setProfilePhotoUrl(String profilePhotoUrl) { this.profilePhotoUrl = profilePhotoUrl; }

    public boolean isNotificationsEnabled() { return notificationsEnabled; }
    public void setNotificationsEnabled(boolean notificationsEnabled) { this.notificationsEnabled = notificationsEnabled; }

    public String getNotificationTime() { return notificationTime; }
    public void setNotificationTime(String notificationTime) { this.notificationTime = notificationTime; }

    public double getInitialWeight() { return initialWeight; }
    public void setInitialWeight(double initialWeight) { this.initialWeight = initialWeight; }

    public String getMetabolismType() { return metabolismType; }
    public void setMetabolismType(String metabolismType) { this.metabolismType = metabolismType; }
}