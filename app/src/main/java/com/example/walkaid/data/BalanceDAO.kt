package com.example.walkaid.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.sql.Timestamp

@Dao
interface BalanceDAO {

    @Insert
    fun insert(record: Balance)

    @Update
    fun update(record: Balance)

    @Query("SELECT * FROM balance where timestamp = :timestamp")
    fun get(timestamp: Long): Balance

    @Query("DELETE FROM balance")
    fun clear()

}