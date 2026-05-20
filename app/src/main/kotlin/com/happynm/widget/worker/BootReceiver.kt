package com.happynm.widget.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.happynm.widget.service.WidgetRefreshService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            WidgetRefreshService.start(context)
        }
    }
}
