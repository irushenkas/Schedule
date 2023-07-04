package com.example.schedule.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.schedule.db.AppDb
import com.example.schedule.dto.Schedule
import com.example.schedule.entity.ScheduleEntity

class ScheduleRepositoryImpl(
    context: Context,
) : ScheduleRepository {

    private val dao = AppDb.getInstance(context = context).scheduleDao()

    override fun getAll(): LiveData<List<Schedule>> {
        return dao.getAll().map { list ->
            list.map {
                it.toDto()
            }
        }
    }

    override fun save(schedule: Schedule) {
        dao.save(ScheduleEntity.fromDto(schedule))
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }
}