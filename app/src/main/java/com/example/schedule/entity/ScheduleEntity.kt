package com.example.schedule.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.schedule.dto.Schedule

@Entity
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val isActive: Boolean = true,
) {
    fun toDto() = Schedule(id, name, isActive)

    companion object {
        fun fromDto(dto: Schedule) =
            ScheduleEntity(dto.id, dto.name, dto.isActive)
    }
}