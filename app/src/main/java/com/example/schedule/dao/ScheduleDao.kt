package com.example.schedule.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.schedule.entity.ScheduleEntity

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM ScheduleEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<ScheduleEntity>>

    @Insert
    fun insert(schedule: ScheduleEntity)

    @Query("UPDATE ScheduleEntity SET name = :name, isActive = :isActive WHERE id = :id")
    fun updateContentById(id: Long, name: String, isActive: Boolean)

    fun save(schedule: ScheduleEntity) =
        if (schedule.id == 0L) insert(schedule)
        else updateContentById(schedule.id, schedule.name, schedule.isActive)

    @Query("DELETE FROM ScheduleEntity WHERE id = :id")
    fun removeById(id: Long)
}