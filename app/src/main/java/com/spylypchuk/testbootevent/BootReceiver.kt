package com.spylypchuk.testbootevent

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.spylypchuk.testbootevent.work.BootNotificationWorker
import java.util.concurrent.TimeUnit

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            scheduleNotifications(context)
        }
    }

    private fun scheduleNotifications(context: Context) {
        val workRequest = PeriodicWorkRequest.Builder(
            BootNotificationWorker::class.java,
            15, TimeUnit.MINUTES
        ).build()

        // Enqueue the work request
        WorkManager.getInstance(context).enqueue(workRequest)
    }
}