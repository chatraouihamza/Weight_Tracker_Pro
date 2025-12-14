package com.example.weighttrackerapp.utils;

import android.content.Context;
import android.util.Base64;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for security and encryption operations.
 */
public class SecurityUtils {
    
    private static final String PREFS_NAME = "secure_prefs";
    
    /**
     * Get encrypted SharedPreferences instance.
     */
    public static EncryptedSharedPreferences getEncryptedPreferences(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            
            return (EncryptedSharedPreferences) EncryptedSharedPreferences.create(
                    context,
                    PREFS_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Hash a string using SHA-256.
     */
    public static String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            return Base64.encodeToString(hash, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Encrypt a string using Base64 encoding (simple encryption for non-sensitive data).
     */
    public static String encodeString(String input) {
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }
    
    /**
     * Decrypt a Base64 encoded string.
     */
    public static String decodeString(String encoded) {
        try {
            byte[] decodedBytes = Base64.decode(encoded, Base64.DEFAULT);
            return new String(decodedBytes);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Check if a string is a valid email format.
     */
    public static boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email != null && email.matches(emailPattern);
    }
    
    /**
     * Check if a string is a valid password (minimum 8 characters, at least one uppercase, one lowercase, one digit).
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUpperCase = password.matches(".*[A-Z].*");
        boolean hasLowerCase = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        
        return hasUpperCase && hasLowerCase && hasDigit;
    }
}
