package com.happynm.widget

import android.app.Application
import androidx.work.*
import com.happynm.widget.worker.WidgetAlarmReceiver
import com.happynm.widget.worker.WidgetUpdateWorker
import java.util.concurrent.TimeUnit

class HappyNMApp : Application() {

    override fun onCreate() {
        super.onCreate()
        scheduleWidgetUpdate()
        WidgetAlarmReceiver.schedule(this)
    }

    private fun scheduleWidgetUpdate() {
        val workRequest = PeriodicWorkRequestBuilder<WidgetUpdateWorker>(
            15, TimeUnit.MINUTES
        ).setConstraints(
            Constraints.Builder()
                .setRequiresBatteryNotLow(false)
                .build()
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "widget_update",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}
