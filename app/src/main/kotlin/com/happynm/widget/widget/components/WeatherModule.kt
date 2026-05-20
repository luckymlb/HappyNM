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
import com.happynm.widget.data.model.WeatherInfo

@Composable
fun WeatherModule(weather: WeatherInfo?, modifier: GlanceModifier = GlanceModifier) {
    Column(
        modifier = modifier
            .background(ColorProvider(Color(0xFF3A3A2A)))
            .padding(10.dp)
            .cornerRadius(12.dp)
    ) {
        Text(
            text = "☁️",
            style = TextStyle(fontSize = 20.sp)
        )
        Spacer(modifier = GlanceModifier.height(4.dp))
        Text(
            text = weather?.tempRangeText ?: "--°C",
            style = TextStyle(
                color = ColorProvider(Color(0xFFFBBF24)),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}
