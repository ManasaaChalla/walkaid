package com.example.walkaid.data

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.sql.Timestamp

@Dao
interface SessionsDAO {

    @Insert
    fun insert(record: Sessions)

    @Update
    fun update(record: Sessions)

    @Query("SELECT * FROM sessions where sessionId = :sessionId")
    fun get(sessionId: Long): Sessions

    @Query("SELECT COUNT(*) FROM sessions")
    fun getSessionsCount(): Int

    @Query("SELECT * FROM sessions")
    fun getAllSessions(): List<Sessions>

    @Query("DELETE FROM sessions")
    fun clear()

}