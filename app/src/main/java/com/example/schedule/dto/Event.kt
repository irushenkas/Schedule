package com.example.schedule.dto

data class Event(
    val id: Long,
    val scheduleId: Long,
    val name: String,
    val description: String?,
    val start: String,
    val finish: String,
    val notifyBeforeMinutes: Int,
    var shouldNotify: Boolean = false,
)