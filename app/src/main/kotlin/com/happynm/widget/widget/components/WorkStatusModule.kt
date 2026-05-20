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
    val progressPercent = (status.progress * 100).toInt()

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
        }
        Spacer(modifier = GlanceModifier.height(6.dp))
        // 进度条：轨道 + 填充
        Box(
            modifier = GlanceModifier
                .fillMaxWidth()
                .height(8.dp)
                .cornerRadius(4.dp)
                .background(ColorProvider(Color(0xFFE5E7EB)))
        ) {
            Row(modifier = GlanceModifier.fillMaxWidth()) {
                if (progressPercent > 0) {
                    Box(
                        modifier = GlanceModifier
                            .height(8.dp)
                            .width((progressPercent * 2).dp.coerceAtMost(200.dp))
                            .cornerRadius(4.dp)
                            .background(ColorProvider(Color(0xFF4A90D9)))
                    ) {}
                }
                Spacer(modifier = GlanceModifier.defaultWeight())
            }
        }
        Spacer(modifier = GlanceModifier.height(4.dp))
        Row(modifier = GlanceModifier.fillMaxWidth()) {
            Spacer(modifier = GlanceModifier.defaultWeight())
            Text(
                text = "${progressPercent}%",
                style = TextStyle(
                    color = ColorProvider(Color(0xFF4A90D9)),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}
