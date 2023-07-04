package com.example.schedule.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.schedule.db.AppDb
import com.example.schedule.dto.Event
import com.example.schedule.entity.EventEntity
import com.example.schedule.service.RemindersManager
import java.util.*


class EventRepositoryImpl(
    private val context: Context,
) : EventRepository {

    private val dao = AppDb.getInstance(context = context).eventDao()

    override fun getAll(scheduleId: Long): LiveData<List<Event>> {
        return dao.getAll().map { list ->
            list.map {
                it.toDto()
            }
        }
    }

    override fun save(event: Event) {
        dao.save(EventEntity.fromDto(event))
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }

    override fun removeByScheduleId(id: Long) {
        val events = dao.getBySchedule(id).map {
            it.toDto()
        }
        for (event in events) {
            removeById(event.id)
        }
    }

    override fun startNotifications(scheduleId: Long) {
        val manager = RemindersManager(context)

        val events = dao.getBySchedule(scheduleId).map {
                it.toDto()
            }
        for (event in events) {
            manager.startReminder(event.id, event.name, event.description,
                event.start, event.finish, event.notifyBeforeMinutes)
        }
    }

    override fun stopNotifications(scheduleId: Long) {
        val manager = RemindersManager(context)

        val events = dao.getBySchedule(scheduleId).map {
            it.toDto()
        }
        for (event in events) {
            manager.stopReminder(event.id.toInt())
        }
    }

    override fun repeatNotification(eventId: Long, repeatAfterMinutes: Int) {
        val event = dao.getById(eventId)

        val time = event.start.split(":")?.toTypedArray()
        if(time == null || time.size != 2) {
            return
        }

        val calendarEvent: Calendar = Calendar.getInstance(Locale.ENGLISH).apply {
            set(Calendar.HOUR_OF_DAY, time[0].toInt())
            set(Calendar.MINUTE, time[1].toInt())
        }
        calendarEvent.apply { add(Calendar.MINUTE, -repeatAfterMinutes) }

        val calendarCurrentTime = Calendar.getInstance(Locale.ENGLISH)

        val diffMinutes = (calendarEvent.timeInMillis - calendarCurrentTime.timeInMillis) / 60_000

        val manager = RemindersManager(context)
        manager.startReminder(event.id, event.name, event.description,
            event.start, event.finish, diffMinutes.toInt())
    }
}