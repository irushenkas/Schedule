package com.example.schedule.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.schedule.dao.EventDao
import com.example.schedule.dao.ScheduleDao
import com.example.schedule.entity.EventEntity
import com.example.schedule.entity.ScheduleEntity


@Database(entities = [ScheduleEntity::class, EventEntity::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDb::class.java, "app.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
    }
}