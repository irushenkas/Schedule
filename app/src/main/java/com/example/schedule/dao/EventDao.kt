package com.example.schedule.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.schedule.entity.EventEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM EventEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<EventEntity>>

    @Insert
    fun insert(event: EventEntity)

    @Query("UPDATE EventEntity SET scheduleId = :scheduleId, name = :name, " +
            " description = :description, start = :start, " +
            "finish = :finish, shouldNotify = :shouldNotify WHERE id = :id")
    fun updateContentById(id: Long, scheduleId: Long, name: String, description: String?,
        start: String, finish: String, shouldNotify: Boolean)

    fun save(event: EventEntity) =
        if (event.id == 0L) insert(event)
        else updateContentById(event.id, event.scheduleId, event.name, event.description,
            event.start, event.finish, event.shouldNotify)

    @Query("DELETE FROM EventEntity WHERE id = :id")
    fun removeById(id: Long)

    @Query("SELECT * FROM EventEntity WHERE scheduleId = :id ORDER BY id DESC")
    fun getBySchedule(id: Long): List<EventEntity>

    @Query("SELECT * FROM EventEntity WHERE id = :id LIMIT 1")
    fun getById(id: Long): EventEntity
}