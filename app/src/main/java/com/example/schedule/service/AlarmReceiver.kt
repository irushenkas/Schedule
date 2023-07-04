package com.example.schedule.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.schedule.R
import com.example.schedule.activity.AppActivity
import com.example.schedule.activity.EventFragment
import java.util.*
import kotlin.random.Random


class AlarmReceiver : BroadcastReceiver() {
    private val channelId = "schedule_notification"
    private val repeatAfterMinutes = 5

    override fun onReceive(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, context.resources.getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = context.resources.getString(R.string.channel_description)
            channel.enableLights(true)
            channel.enableVibration(false)

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val bundleShowEvent = createEventBundle(intent.extras?.getLong("id"),
            intent.extras?.getString("name"), intent.extras?.getString("description"),
            intent.extras?.getString("timeStart"), intent.extras?.getString("timeFinish"), false)

        val showEventIntent = NavDeepLinkBuilder(context)
            .setComponentName(AppActivity::class.java)
            .setGraph(R.navigation.nav_main)
            .setDestination(R.id.eventDetailsFragment)
            .setArguments(bundleShowEvent)
            .createPendingIntent()

        var builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_alarm_24dp)
            .setContentTitle(intent.extras?.getString("name"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentText(context.resources.getString(R.string.notification_text, intent.extras?.getInt("notifyBeforeMinutes")))
            .setAutoCancel(true)
            .setContentIntent(showEventIntent)

        val time = intent.extras?.getString("timeStart")?.split(":")?.toTypedArray()
        if(time != null && time.size == 2) {
            val calendar: Calendar = Calendar.getInstance(Locale.ENGLISH).apply {
                set(Calendar.HOUR_OF_DAY, time[0].toInt())
                set(Calendar.MINUTE, time[1].toInt())
            }
            calendar.apply { add(Calendar.MINUTE, -repeatAfterMinutes) }
            if (Calendar.getInstance(Locale.ENGLISH).apply { add(Calendar.MINUTE, 1) }.timeInMillis - calendar.timeInMillis < 0) {
                val bundleRepeatNotification = createEventBundle(intent.extras?.getLong("id"),
                    intent.extras?.getString("name"), intent.extras?.getString("description"),
                    intent.extras?.getString("timeStart"), intent.extras?.getString("timeFinish"), true)

                val repeatNotificationIntent = NavDeepLinkBuilder(context)
                    .setComponentName(AppActivity::class.java)
                    .setGraph(R.navigation.nav_main)
                    .setDestination(R.id.eventDetailsFragment)
                    .setArguments(bundleRepeatNotification)
                    .createPendingIntent()

                builder.addAction(R.drawable.ic_repeat_24dp, context.resources.getString(R.string.notification_repeat, repeatAfterMinutes),
                    repeatNotificationIntent);
            }
        }

        with(NotificationManagerCompat.from(context)) {
            notify(Random.nextInt(100_000), builder.build())
        }
    }

    private fun createEventBundle(id: Long?, name: String?, description: String?, start: String?, finish: String?, repeat: Boolean): Bundle {
        val bundle = Bundle()
        bundle.putLong("id", id!!)
        bundle.putString("name", name)
        bundle.putString("description", description)
        bundle.putString("timeStart", start)
        bundle.putString("timeFinish", finish)
        bundle.putBoolean("repeat", repeat)
        bundle.putInt("repeatAfterMinutes", repeatAfterMinutes)
        return bundle
    }
}