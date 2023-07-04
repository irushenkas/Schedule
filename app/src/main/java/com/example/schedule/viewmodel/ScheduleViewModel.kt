package com.example.schedule.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.schedule.dto.Schedule
import com.example.schedule.repository.EventRepository
import com.example.schedule.repository.EventRepositoryImpl
import com.example.schedule.repository.ScheduleRepository
import com.example.schedule.repository.ScheduleRepositoryImpl

private val empty = Schedule(
    id = 0,
    name = "",
)

class ScheduleViewModel(application: Application) : AndroidViewModel(application) {
    private val scheduleRepository: ScheduleRepository = ScheduleRepositoryImpl(application)
    private val eventRepository: EventRepository = EventRepositoryImpl(application)

    val data = scheduleRepository.getAll()
    private val edited = MutableLiveData(empty)

    fun save() {
        edited.value?.let {
            scheduleRepository.save(it)
        }
        edited.value = empty
    }

    fun edit(schedule: Schedule) {
        edited.value = schedule
    }

    fun changeContent(content: String) {
        val text = content.trim()
        edited.value = edited.value?.copy(name = text)
    }

    fun removeById(id: Long) {
        eventRepository.removeByScheduleId(id)
        scheduleRepository.removeById(id)
    }

    fun changeActive(schedule: Schedule) {
        if(!schedule.isActive) {
            eventRepository.startNotifications(schedule.id)

            val currentActive = data.value?.firstOrNull { it.isActive }
            if(currentActive != null) {
                eventRepository.stopNotifications(currentActive.id)

                edit(currentActive)
                setActive(!currentActive.isActive)
                save()
            }
        } else {
            eventRepository.stopNotifications(schedule.id)
        }

        edit(schedule)
        setActive(!schedule.isActive)
        save()
    }

    private fun setActive(isActive: Boolean) {
        edited.value = edited.value?.copy(isActive = isActive)
    }
}