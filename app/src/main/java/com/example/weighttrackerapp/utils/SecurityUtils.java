package com.example.weighttrackerapp.utils;

import android.content.Context;
import android.util.Base64;
import android.util.Patterns; // Use Android's robust patterns

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
     * Useful if storing sensitive tokens, though our SessionManager currently uses standard Prefs.
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
     * IMPORTANT: Used for passwords before saving to DB or comparing in Login.
     */
    public static String hashString(String input) {
        if (input == null) return null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            // CHANGED: Use NO_WRAP to avoid adding newlines to the hash string
            return Base64.encodeToString(hash, Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Encrypt a string using Base64 encoding.
     * Note: This is ENCODING, not true Encryption. Good for obfuscation only.
     */
    public static String encodeString(String input) {
        if (input == null) return null;
        return Base64.encodeToString(input.getBytes(), Base64.NO_WRAP);
    }

    /**
     * Decrypt a Base64 encoded string.
     */
    public static String decodeString(String encoded) {
        if (encoded == null) return null;
        try {
            byte[] decodedBytes = Base64.decode(encoded, Base64.NO_WRAP);
            return new String(decodedBytes);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Check if a string is a valid email format using Android's built-in patterns.
     */
    public static boolean isValidEmail(String email) {
        return email != null && !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Check if a string is a valid password.
     * Rule: Min 8 chars, 1 Uppercase, 1 Lowercase, 1 Digit.
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