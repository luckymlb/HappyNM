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

    Column(
        modifier = modifier
            .background(ColorProvider(Color(0xFF1A3A2A)))
            .padding(12.dp)
            .cornerRadius(12.dp)
    ) {
        Text(
            text = "今日已赚",
            style = TextStyle(
                color = ColorProvider(Color(0xFF34C759)),
                fontSize = 13.sp
            )
        )
        Spacer(modifier = GlanceModifier.height(4.dp))
        if (status.isWorkDay) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "¥",
                    style = TextStyle(
                        color = ColorProvider(Color(0xFF34C759)),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = "%.2f".format(status.earned),
                    style = TextStyle(
                        color = ColorProvider(Color(0xFF34C759)),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        } else {
            Text(
                text = "今日休息",
                style = TextStyle(
                    color = ColorProvider(Color(0xFF8E8E93)),
                    fontSize = 20.sp
                )
            )
        }
    }
}
