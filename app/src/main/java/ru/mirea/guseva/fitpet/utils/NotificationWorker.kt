package ru.mirea.guseva.fitpet.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import ru.mirea.guseva.fitpet.R
import java.text.SimpleDateFormat
import java.util.*

class NotificationWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    companion object {
        const val CHANNEL_ID = "EVENT_NOTIFICATION_CHANNEL"
    }

    override fun doWork(): Result {
        val eventId = inputData.getInt("event_id", -1)
        val animalName = inputData.getString("animal_name")
        val eventTime = inputData.getLong("event_time", 0L)

        if (eventId == -1 || animalName.isNullOrEmpty() || eventTime == 0L) {
            return Result.failure()
        }

        createNotificationChannel()

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.cat)
            .setContentTitle("Напоминание о событии")
            .setContentText("Событие $animalName завтра в ${SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(eventTime))}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(eventId, notification)

        return Result.success()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Event Notifications"
            val descriptionText = "Notifications for upcoming events"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
