package com.example.weighttrackerapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.weighttrackerapp.models.Measurement;

import java.util.Date;
import java.util.List;

/**
 * Data Access Object (DAO) for Measurement entities.
 */
@Dao
public interface MeasurementDao {
    
    @Insert
    long insert(Measurement measurement);
    
    @Update
    void update(Measurement measurement);
    
    @Delete
    void delete(Measurement measurement);
    
    @Query("SELECT * FROM measurements ORDER BY date DESC")
    LiveData<List<Measurement>> getAllMeasurements();
    
    @Query("SELECT * FROM measurements WHERE id = :id")
    LiveData<Measurement> getMeasurementById(int id);
    
    @Query("SELECT * FROM measurements ORDER BY date DESC LIMIT 1")
    LiveData<Measurement> getLatestMeasurement();
    
    @Query("SELECT * FROM measurements WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    LiveData<List<Measurement>> getMeasurementsBetweenDates(Date startDate, Date endDate);
    
    @Query("DELETE FROM measurements")
    void deleteAll();
    
    @Query("SELECT COUNT(*) FROM measurements")
    LiveData<Integer> getTotalMeasurementsCount();
}
