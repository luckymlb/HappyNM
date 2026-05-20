package com.happynm.widget.worker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.updateAll
import com.happynm.widget.widget.MainWidget
import com.happynm.widget.widget.SalaryWidget
import kotlinx.coroutines.runBlocking

class WidgetAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        runBlocking {
            MainWidget().updateAll(context)
            SalaryWidget().updateAll(context)
        }
        scheduleNext(context)
    }

    companion object {
        private const val REQUEST_CODE = 1001

        fun schedule(context: Context) {
            scheduleNext(context)
        }

        fun cancel(context: Context) {
            val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            am.cancel(getPendingIntent(context))
        }

        private fun scheduleNext(context: Context) {
            val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val triggerAt = System.currentTimeMillis() + 60_000L
            try {
                am.setExactAndAllowWhileIdle(
                    AlarmManager.RTC,
                    triggerAt,
                    getPendingIntent(context)
                )
            } catch (e: SecurityException) {
                am.set(AlarmManager.RTC, triggerAt, getPendingIntent(context))
            }
        }

        private fun getPendingIntent(context: Context): PendingIntent {
            val intent = Intent(context, WidgetAlarmReceiver::class.java)
            return PendingIntent.getBroadcast(
                context,
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
    }
}
