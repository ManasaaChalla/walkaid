package com.example.walkaid.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "balance")
data class Balance(
    @PrimaryKey(autoGenerate = false)
    var timestamp: Long,

    @ColumnInfo(name = "COGFront")
    var COGFront: Int,

    @ColumnInfo(name = "COGBack")
    var COGBack: Int,

    @ColumnInfo(name = "COGLeft")
    var COGLeft: Int,

    @ColumnInfo(name = "COGRight")
    var COGRight: Int,

    @ColumnInfo(name = "COPLeftFront")
    var COPLeftFront: Int,

    @ColumnInfo(name = "COPLeftBack")
    var COPLeftBack: Int,

    @ColumnInfo(name = "COPRightFront")
    var COPRightFront: Int,

    @ColumnInfo(name = "COPRighBack")
    var COPRighBack: Int,

)