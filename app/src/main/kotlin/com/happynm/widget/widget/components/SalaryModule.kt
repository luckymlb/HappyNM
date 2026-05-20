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
fun SalaryModule(settings: UserSettings, modifier: GlanceModifier = GlanceModifier) {
    val status = SalaryCalculator.calculate(settings)
    val progressPercent = (status.progress * 100).toInt()

    Column(
        modifier = modifier
            .background(ColorProvider(Color(0xFFEDF8F0)))
            .padding(12.dp)
            .cornerRadius(14.dp)
    ) {
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "今日已赚",
                style = TextStyle(
                    color = ColorProvider(Color(0xFF1B9E5A)),
                    fontSize = 11.sp
                )
            )
            Spacer(modifier = GlanceModifier.defaultWeight())
            if (status.isWorkDay && status.dailySalary > 0) {
                Text(
                    text = "日薪 ¥${"%.0f".format(status.dailySalary)}",
                    style = TextStyle(
                        color = ColorProvider(Color(0xFF9CA3AF)),
                        fontSize = 10.sp
                    )
                )
            }
        }
        Spacer(modifier = GlanceModifier.height(2.dp))
        if (status.isWorkDay) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "¥",
                    style = TextStyle(
                        color = ColorProvider(Color(0xFF2ECC71)),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = "%.2f".format(status.earned),
                    style = TextStyle(
                        color = ColorProvider(Color(0xFF1B9E5A)),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Spacer(modifier = GlanceModifier.height(6.dp))
            // 进度条内嵌
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "● ${status.statusText}",
                    style = TextStyle(
                        color = ColorProvider(if (status.isWorking) Color(0xFF2ECC71) else Color(0xFF9CA3AF)),
                        fontSize = 10.sp
                    )
                )
                Spacer(modifier = GlanceModifier.defaultWeight())
                Text(
                    text = "${progressPercent}%",
                    style = TextStyle(
                        color = ColorProvider(Color(0xFF4A90D9)),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Spacer(modifier = GlanceModifier.height(4.dp))
            Box(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .cornerRadius(3.dp)
                    .background(ColorProvider(Color(0xFFD1FAE5)))
            ) {
                Row(modifier = GlanceModifier.fillMaxWidth()) {
                    if (progressPercent > 0) {
                        Box(
                            modifier = GlanceModifier
                                .height(5.dp)
                                .width((progressPercent * 2).dp.coerceAtMost(200.dp))
                                .cornerRadius(3.dp)
                                .background(ColorProvider(Color(0xFF2ECC71)))
                        ) {}
                    }
                    Spacer(modifier = GlanceModifier.defaultWeight())
                }
            }
        } else {
            Text(
                text = "休息日 ☀️",
                style = TextStyle(
                    color = ColorProvider(Color(0xFF6B7280)),
                    fontSize = 18.sp
                )
            )
        }
    }
}
