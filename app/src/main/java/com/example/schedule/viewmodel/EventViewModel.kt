package com.example.schedule.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.schedule.activity.EventDetailsFragment.Companion.repeatAfterMinutes
import com.example.schedule.dto.Event
import com.example.schedule.repository.EventRepository
import com.example.schedule.repository.EventRepositoryImpl

private val empty = Event(
    id = 0,
    scheduleId = 0,
    name = "",
    description = "",
    start = "",
    finish = "",
    notifyBeforeMinutes = 0,
    shouldNotify = false,
)

class EventViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {
    private val scheduleId: Long? = savedStateHandle.get<Long>("scheduleId")
    private val repository: EventRepository = EventRepositoryImpl(application)

    val data = scheduleId?.let { repository.getAll(it) }
    private val edited = MutableLiveData(empty)

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun edit(event: Event) {
        edited.value = event
    }

    fun changeContent(scheduleId: Long, content: String, description: String?, start: String,
            finish: String, notifyBeforeMinutes: Int, notify: Boolean) {
        val text = content.trim()
        edited.value = edited.value?.copy(scheduleId = scheduleId, name = text, description = description,
            start = start, finish = finish, notifyBeforeMinutes = notifyBeforeMinutes, shouldNotify = notify)
    }

    fun removeById(id: Long) = repository.removeById(id)

    fun notify(event: Event) {
        edit(event)
        edited.value = edited.value?.copy(shouldNotify = !event.shouldNotify)
        save()
    }

    fun repeatNotification(eventId: Long, repeatAfterMinutes: Int) {
        repository.repeatNotification(eventId, repeatAfterMinutes)
    }
}