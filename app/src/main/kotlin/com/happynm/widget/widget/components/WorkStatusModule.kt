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

    val statusColor = if (status.isWorking) Color(0xFF2ECC71) else Color(0xFF9CA3AF)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ColorProvider(Color(0xFFF5F7FA)))
            .padding(10.dp)
            .cornerRadius(14.dp)
    ) {
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "● ${status.statusText}",
                style = TextStyle(
                    color = ColorProvider(statusColor),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            )
            Spacer(modifier = GlanceModifier.defaultWeight())
            Text(
                text = timeRange,
                style = TextStyle(
                    color = ColorProvider(Color(0xFF9CA3AF)),
                    fontSize = 11.sp
                )
            )
            Spacer(modifier = GlanceModifier.width(8.dp))
            Text(
                text = "${(status.progress * 100).toInt()}%",
                style = TextStyle(
                    color = ColorProvider(Color(0xFF4A90D9)),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
