package com.example.schedule.repository

import androidx.lifecycle.LiveData
import com.example.schedule.dto.Event

interface EventRepository {
    fun getAll(scheduleId: Long): LiveData<List<Event>>
    fun save(event: Event)
    fun removeById(id: Long)
    fun removeByScheduleId(id: Long)
    fun startNotifications(scheduleId: Long)
    fun stopNotifications(scheduleId: Long)
    fun repeatNotification(eventId: Long, repeatAfterMinutes: Int)
}