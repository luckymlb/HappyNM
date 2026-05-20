package com.happynm.widget

import android.app.Application
import androidx.glance.appwidget.updateAll
import androidx.work.*
import com.happynm.widget.widget.MainWidget
import com.happynm.widget.widget.SalaryWidget
import com.happynm.widget.worker.WidgetAlarmReceiver
import com.happynm.widget.worker.WidgetUpdateWorker
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class HappyNMApp : Application() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        scheduleWidgetUpdate()
        WidgetAlarmReceiver.schedule(this)
        startRealTimeRefresh()
    }

    private fun startRealTimeRefresh() {
        scope.launch {
            while (isActive) {
                delay(3000L)
                try {
                    MainWidget().updateAll(this@HappyNMApp)
                    SalaryWidget().updateAll(this@HappyNMApp)
                } catch (_: Exception) {}
            }
        }
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
