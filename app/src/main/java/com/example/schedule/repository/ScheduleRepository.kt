package com.example.schedule.repository

import androidx.lifecycle.LiveData
import com.example.schedule.dto.Schedule

interface ScheduleRepository {
    fun getAll(): LiveData<List<Schedule>>
    fun save(schedule: Schedule)
    fun removeById(id: Long)
}