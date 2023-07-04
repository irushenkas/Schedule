package com.example.schedule.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.*

class RemindersManager (
    private val context: Context,
    ) {

    fun startReminder(
        id: Long,
        name: String,
        description: String?,
        timeStart: String,
        timeFinish: String,
        notifyBeforeMinutes: Int,
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent =
            Intent(context.applicationContext, AlarmReceiver::class.java)
        intent.putExtra("id", id)
        intent.putExtra("name", name)
        intent.putExtra("description", description)
        intent.putExtra("timeStart", timeStart)
        intent.putExtra("timeFinish", timeFinish)
        intent.putExtra("notifyBeforeMinutes", notifyBeforeMinutes)
        val pendingIntent = intent.let { intent ->
            PendingIntent.getBroadcast(
                context.applicationContext,
                id.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val time = timeStart.split(":").toTypedArray()

        val calendar: Calendar = Calendar.getInstance(Locale.ENGLISH).apply {
            set(Calendar.HOUR_OF_DAY, time[0].toInt())
            set(Calendar.MINUTE, time[1].toInt())
        }

        calendar.apply { add(Calendar.MINUTE, -notifyBeforeMinutes) }

        //if (Calendar.getInstance(Locale.ENGLISH).apply { add(Calendar.MINUTE, 1) }.timeInMillis - calendar.timeInMillis < 0) {
            alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(calendar.timeInMillis, pendingIntent),
                pendingIntent)
        //}
    }

    fun stopReminder(
        reminderId: Int
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                context,
                reminderId,
                intent,
                0
            )
        }
        alarmManager.cancel(intent)
    }
}