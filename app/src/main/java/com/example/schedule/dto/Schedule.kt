package com.example.schedule.dto

data class Schedule(
    val id: Long,
    val name: String,
    var isActive: Boolean = false
)