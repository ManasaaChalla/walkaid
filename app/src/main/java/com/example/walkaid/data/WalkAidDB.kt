package com.example.walkaid.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.security.AccessControlContext

@Database(entities=[Accelerometer::class, Balance::class, Sessions::class], version = 1, exportSchema = false)
abstract class WalkAidDB : RoomDatabase() {

    abstract val accelerometerDAO: AccelerometerDAO
    abstract val balanceDAO: BalanceDAO
    abstract val sessionsDAO: SessionsDAO

    companion object {
        @Volatile
        private var INSTANCE: WalkAidDB? = null

        fun getInstance(context: Context): WalkAidDB {
            Log.v("TAG","TEST DB CREATION")
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WalkAidDB::class.java,
                        "WalkAid"
                    )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}