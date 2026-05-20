package com.happynm.widget.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.glance.appwidget.updateAll
import com.happynm.widget.MainActivity
import com.happynm.widget.R
import com.happynm.widget.data.datastore.SettingsKeys
import com.happynm.widget.data.datastore.settingsDataStore
import com.happynm.widget.widget.MainWidget
import com.happynm.widget.widget.SalaryWidget
import kotlinx.coroutines.*
import androidx.datastore.preferences.core.edit

class WidgetRefreshService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val refreshRunnable = object : Runnable {
        override fun run() {
            scope.launch {
                try {
                    settingsDataStore.edit { prefs ->
                        prefs[SettingsKeys.REFRESH_TICK] = System.currentTimeMillis()
                    }
                    MainWidget().updateAll(this@WidgetRefreshService)
                    SalaryWidget().updateAll(this@WidgetRefreshService)
                } catch (_: Exception) {}
            }
            handler.postDelayed(this, 10_000L)
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        val notification = buildNotification()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
        handler.post(refreshRunnable)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "薪资实时更新",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "保持小组件金额实时跳动"
                setShowBadge(false)
            }
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pi = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("搞钱中...")
            .setContentText("小组件正在实时更新")
            .setContentIntent(pi)
            .setOngoing(true)
            .setSilent(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        handler.removeCallbacks(refreshRunnable)
        scope.cancel()
        super.onDestroy()
    }

    companion object {
        private const val CHANNEL_ID = "widget_refresh"
        private const val NOTIFICATION_ID = 1001

        fun start(context: Context) {
            val intent = Intent(context, WidgetRefreshService::class.java)
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
            } catch (_: Exception) {}
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, WidgetRefreshService::class.java))
        }
    }
}
