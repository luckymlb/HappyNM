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
            .background(ColorProvider(Color(0xFFEFF6FF)))
            .padding(10.dp)
            .cornerRadius(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "☀️",
            style = TextStyle(fontSize = 18.sp)
        )
        Spacer(modifier = GlanceModifier.height(2.dp))
        Text(
            text = weather?.tempRangeText ?: "--°C",
            style = TextStyle(
                color = ColorProvider(Color(0xFF4A90D9)),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}
