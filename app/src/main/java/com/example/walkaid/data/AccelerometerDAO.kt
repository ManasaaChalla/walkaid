package com.example.walkaid.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.sql.Timestamp

@Dao
interface AccelerometerDAO {

    @Insert
    fun insert(record: Accelerometer)

    @Update
    fun update(record: Accelerometer)

    @Query("SELECT * FROM accelerometer where timestamp = :timestamp")
    fun get(timestamp: Long): Accelerometer

    @Query("DELETE FROM accelerometer")
    fun clear()

}