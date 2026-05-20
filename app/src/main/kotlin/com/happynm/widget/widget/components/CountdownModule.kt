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
fun CountdownModule(settings: UserSettings, modifier: GlanceModifier = GlanceModifier) {
    val days = SalaryCalculator.getCountdownDays(settings.targetDate)

    Column(
        modifier = modifier
            .background(ColorProvider(Color(0xFF3A2A2A)))
            .padding(10.dp)
            .cornerRadius(12.dp)
    ) {
        Text(
            text = "📅",
            style = TextStyle(fontSize = 20.sp)
        )
        Spacer(modifier = GlanceModifier.height(4.dp))
        if (days != null && days >= 0) {
            Text(
                text = "${days}天",
                style = TextStyle(
                    color = ColorProvider(Color(0xFFFF6B8A)),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        } else {
            Text(
                text = "--天",
                style = TextStyle(
                    color = ColorProvider(Color(0xFF8E8E93)),
                    fontSize = 16.sp
                )
            )
        }
    }
}
