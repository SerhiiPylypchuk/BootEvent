package com.spylypchuk.testbootevent.work

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.spylypchuk.testbootevent.data.BootEventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.joda.time.format.DateTimeFormat
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

const val DATE_TIME_FORMAT = "DD/MM/YYYY HH:MM:SS"

class BootNotificationWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams), KoinComponent {

    private val repository: BootEventRepository by inject()

    override suspend fun doWork(): Result {

        saveEvent()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                return Result.failure()
            }
        }

        getInfoForNotification()

        return Result.success()
    }

    private suspend fun saveEvent() {
        withContext(Dispatchers.IO) {
            repository.saveBootEvent()
        }
    }

    private suspend fun getInfoForNotification() {
        withContext(Dispatchers.IO) {
            val lastBoot = repository.getLastBootEvent()
            val secondLastBoot = repository.getSecondLastBootEvent()

            withContext(Dispatchers.Main) {
                when{
                    lastBoot == null -> showNotification("No boots detected")
                    secondLastBoot == null -> showNotification("The boot was detected = ${formatDate(lastBoot.timestamp)}")
                    else -> {
                        val delta = lastBoot.timestamp - secondLastBoot.timestamp
                        showNotification("Last boots time delta = ${delta / 60000} minutes")
                    }
                }
            }
        }


    }

    private fun showNotification(content: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "boot_notification_channel",
                "Boot Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, "boot_notification_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Boot Event Notification")
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        //TODO - make dismiss logic

        notificationManager.notify(1, notification)
    }

    private fun formatDate(timestamp: Long) = DateTimeFormat.forPattern(DATE_TIME_FORMAT).print(timestamp)
}