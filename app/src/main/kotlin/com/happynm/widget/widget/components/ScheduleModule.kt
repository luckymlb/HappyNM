package com.happynm.widget.widget.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.background
import androidx.glance.appwidget.cornerRadius
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.happynm.widget.MainActivity

@Composable
fun ScheduleModule(eventCount: Int = 0, modifier: GlanceModifier = GlanceModifier) {
    Column(
        modifier = modifier
            .background(ColorProvider(Color(0xFFFFF5EB)))
            .padding(6.dp)
            .cornerRadius(12.dp)
            .clickable(actionStartActivity<MainActivity>()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$eventCount",
            style = TextStyle(
                color = ColorProvider(Color(0xFFF39C12)),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = "日程",
            style = TextStyle(
                color = ColorProvider(Color(0xFF9CA3AF)),
                fontSize = 10.sp
            )
        )
    }
}
