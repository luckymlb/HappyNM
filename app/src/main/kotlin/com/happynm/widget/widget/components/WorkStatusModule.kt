package com.happynm.widget.widget.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.background
import androidx.glance.appwidget.cornerRadius
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.happynm.widget.data.model.UserSettings
import com.happynm.widget.domain.SalaryCalculator

@Composable
fun WorkStatusModule(settings: UserSettings, modifier: GlanceModifier = GlanceModifier) {
    val status = SalaryCalculator.calculate(settings)
    val timeRange = "%d:%02d – %d:%02d".format(
        settings.workStartHour, settings.workStartMinute,
        settings.workEndHour, settings.workEndMinute
    )

    val statusColor = if (status.isWorking) Color(0xFF34C759) else Color(0xFF8E8E93)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ColorProvider(Color(0xFF2C2C2E)))
            .padding(12.dp)
            .cornerRadius(12.dp)
    ) {
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "● ${status.statusText}",
                style = TextStyle(
                    color = ColorProvider(statusColor),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            )
            Spacer(modifier = GlanceModifier.defaultWeight())
            Text(
                text = timeRange,
                style = TextStyle(
                    color = ColorProvider(Color(0xFF8E8E93)),
                    fontSize = 12.sp
                )
            )
        }
        Spacer(modifier = GlanceModifier.height(6.dp))
        Row(modifier = GlanceModifier.fillMaxWidth()) {
            // Progress track
            Box(
                modifier = GlanceModifier
                    .height(6.dp)
                    .defaultWeight()
                    .background(ColorProvider(Color(0xFF48484A)))
                    .cornerRadius(3.dp)
            ) {}
        }
        // Overlay progress indicator (approximate with text percentage)
        Spacer(modifier = GlanceModifier.height(4.dp))
        Text(
            text = "${(status.progress * 100).toInt()}% 完成",
            style = TextStyle(
                color = ColorProvider(Color(0xFF0A84FF)),
                fontSize = 11.sp
            )
        )
    }
}
