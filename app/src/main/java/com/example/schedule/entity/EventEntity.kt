package com.example.schedule.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.schedule.dto.Event

@Entity
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val scheduleId: Long,
    val name: String,
    val description: String?,
    val start: String,
    val finish: String,
    val notifyBeforeMinutes: Int,
    val shouldNotify: Boolean = true,
) {
    fun toDto() = Event(id, scheduleId, name, description, start, finish, notifyBeforeMinutes, shouldNotify)

    companion object {
        fun fromDto(dto: Event) =
            EventEntity(dto.id, dto.scheduleId, dto.name, dto.description,
                dto.start, dto.finish, dto.notifyBeforeMinutes, dto.shouldNotify)
    }
}