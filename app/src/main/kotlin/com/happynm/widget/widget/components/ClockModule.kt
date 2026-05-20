package com.happynm.widget.widget.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.happynm.widget.domain.LunarCalendar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ClockModule(modifier: GlanceModifier = GlanceModifier) {
    val now = LocalDateTime.now()
    val timeStr = now.format(DateTimeFormatter.ofPattern("HH:mm", Locale.US))
    val today = now.toLocalDate()
    val dayOfWeek = today.format(DateTimeFormatter.ofPattern("EEEE", Locale.CHINESE))
    val dateStr = "${today.monthValue}月${today.dayOfMonth}日"
    val lunarStr = LunarCalendar.getLunarDateString(today)

    Column(modifier = modifier) {
        Text(
            text = timeStr,
            style = TextStyle(
                color = ColorProvider(Color(0xFF1A1A2E)),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = "$dayOfWeek · $dateStr · $lunarStr",
            style = TextStyle(
                color = ColorProvider(Color(0xFF6B7280)),
                fontSize = 11.sp
            )
        )
    }
}
