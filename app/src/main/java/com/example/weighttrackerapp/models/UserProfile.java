package com.example.weighttrackerapp.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.io.Serializable;
import java.util.Date;

/**
 * UserProfile model representing user information.
 */
@Entity(tableName = "user_profile")
public class UserProfile implements Serializable {
    @PrimaryKey
    private int id = 1; // Single user profile
    
    private String name;
    private int age;
    private String gender; // MALE, FEMALE, OTHER
    private double height; // in cm
    private String unitPreference; // KG, LBS
    private Date createdDate;
    private Date lastUpdatedDate;
    private String profilePhotoUrl;
    private boolean notificationsEnabled;
    private String notificationTime; // HH:mm format
    
    // Constructors
    public UserProfile() {
    }
    
    @Ignore
    public UserProfile(String name, int age, String gender, double height) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.unitPreference = "KG";
        this.notificationsEnabled = true;
        this.notificationTime = "08:00";
        this.createdDate = new Date();
        this.lastUpdatedDate = new Date();
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public double getHeight() {
        return height;
    }
    
    public void setHeight(double height) {
        this.height = height;
    }
    
    public String getUnitPreference() {
        return unitPreference;
    }
    
    public void setUnitPreference(String unitPreference) {
        this.unitPreference = unitPreference;
    }
    
    public Date getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }
    
    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
    
    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }
    
    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }
    
    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }
    
    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }
    
    public String getNotificationTime() {
        return notificationTime;
    }
    
    public void setNotificationTime(String notificationTime) {
        this.notificationTime = notificationTime;
    }
    
    @Override
    public String toString() {
        return "UserProfile{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", height=" + height +
                ", unitPreference='" + unitPreference + '\'' +
                '}';
    }
}
