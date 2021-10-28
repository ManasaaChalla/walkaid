package com.example.walkaid.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "accelerometer")
data class Accelerometer(
    @PrimaryKey(autoGenerate = false)
    var timestamp: Long,

    @ColumnInfo(name = "LX")
    var LX: Int,

    @ColumnInfo(name = "LY")
    var LY: Int,
    
    @ColumnInfo(name = "LZ")
    var LZ: Int,
    
    @ColumnInfo(name = "RX")
    var RX: Int,
    
    @ColumnInfo(name = "RY")
    var RY: Int,
    
    @ColumnInfo(name = "RZ")
    var RZ: Int

)