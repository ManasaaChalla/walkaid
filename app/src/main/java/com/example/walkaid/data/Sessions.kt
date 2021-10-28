package com.example.walkaid.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "sessions")
data class Sessions(

    @PrimaryKey(autoGenerate = true)
    val sessionId: Long,

    @ColumnInfo(name = "fromTimestamp")
    val fromTimestamp: Long,

    @ColumnInfo(name = "toTimestamp")
    val toTimestamp: Long,

) {
    constructor(fromTimestamp: Long, toTimestamp: Long): this(0, fromTimestamp, toTimestamp)
}