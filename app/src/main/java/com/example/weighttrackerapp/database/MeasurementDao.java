package com.example.weighttrackerapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.weighttrackerapp.models.Measurement;

import java.util.List;

@Dao
public interface MeasurementDao {

    @Insert
    void insert(Measurement measurement);

    @Update
    void update(Measurement measurement);

    @Delete
    void delete(Measurement measurement);

    @Query("SELECT * FROM measurements WHERE user_id = :userId ORDER BY date_timestamp DESC")
    LiveData<List<Measurement>> getAllMeasurements(int userId);

    @Query("SELECT * FROM measurements WHERE id = :id")
    LiveData<Measurement> getMeasurementById(int id);

    @Query("SELECT * FROM measurements WHERE user_id = :userId ORDER BY date_timestamp DESC LIMIT 1")
    LiveData<Measurement> getLatestMeasurement(int userId);

    @Query("SELECT * FROM measurements WHERE user_id = :userId AND date_timestamp BETWEEN :startTime AND :endTime ORDER BY date_timestamp DESC")
    LiveData<List<Measurement>> getMeasurementsBetweenDates(int userId, long startTime, long endTime);

    @Query("DELETE FROM measurements WHERE user_id = :userId")
    void deleteAll(int userId);

    @Query("SELECT COUNT(*) FROM measurements WHERE user_id = :userId")
    LiveData<Integer> getTotalMeasurementsCount(int userId);
}