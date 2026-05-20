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
            .background(ColorProvider(Color(0xFF2C2C2E)))
            .padding(10.dp)
            .cornerRadius(12.dp)
            .clickable(actionStartActivity<MainActivity>())
    ) {
        Text(
            text = "$eventCount",
            style = TextStyle(
                color = ColorProvider(Color(0xFF0A84FF)),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = GlanceModifier.height(2.dp))
        Text(
            text = "日程",
            style = TextStyle(
                color = ColorProvider(Color(0xFF8E8E93)),
                fontSize = 12.sp
            )
        )
    }
}
