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
    val timeStr = now.format(DateTimeFormatter.ofPattern("h:mm", Locale.US))
    val amPm = now.format(DateTimeFormatter.ofPattern("a", Locale.US)).lowercase()
    val today = now.toLocalDate()
    val dayOfWeek = today.format(DateTimeFormatter.ofPattern("EEEE", Locale.CHINESE))
    val dateStr = "${today.monthValue}月${today.dayOfMonth}日"
    val lunarStr = LunarCalendar.getLunarDateString(today)

    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = timeStr,
                style = TextStyle(
                    color = ColorProvider(Color.White),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = GlanceModifier.width(4.dp))
            Text(
                text = amPm,
                style = TextStyle(
                    color = ColorProvider(Color(0xFF8E8E93)),
                    fontSize = 14.sp
                )
            )
        }
        Text(
            text = "$dayOfWeek · $dateStr · $lunarStr",
            style = TextStyle(
                color = ColorProvider(Color(0xFF8E8E93)),
                fontSize = 12.sp
            )
        )
    }
}
