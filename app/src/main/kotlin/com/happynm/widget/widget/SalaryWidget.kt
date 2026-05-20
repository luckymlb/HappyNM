package com.happynm.widget.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.*
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.happynm.widget.MainActivity
import com.happynm.widget.data.datastore.SettingsKeys
import com.happynm.widget.data.model.UserSettings
import com.happynm.widget.domain.SalaryCalculator

class SalaryWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            SalaryWidgetContent()
        }
    }
}

@Composable
private fun SalaryWidgetContent() {
    val prefs = currentState<Preferences>()
    val settings = UserSettings(
        monthlySalary = prefs[SettingsKeys.MONTHLY_SALARY] ?: 10000.0,
        workStartHour = prefs[SettingsKeys.WORK_START_HOUR] ?: 9,
        workStartMinute = prefs[SettingsKeys.WORK_START_MINUTE] ?: 0,
        workEndHour = prefs[SettingsKeys.WORK_END_HOUR] ?: 18,
        workEndMinute = prefs[SettingsKeys.WORK_END_MINUTE] ?: 0,
        lunchBreakMinutes = prefs[SettingsKeys.LUNCH_BREAK_MINUTES] ?: 60,
        lunchStartHour = prefs[SettingsKeys.LUNCH_START_HOUR] ?: 12,
        lunchStartMinute = prefs[SettingsKeys.LUNCH_START_MINUTE] ?: 0,
        workDays = prefs[SettingsKeys.WORK_DAYS]?.split(",")?.mapNotNull { it.toIntOrNull() }
            ?: listOf(1, 2, 3, 4, 5)
    )
    val status = SalaryCalculator.calculate(settings)

    Row(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(Color.White))
            .cornerRadius(18.dp)
            .padding(14.dp)
            .clickable(actionStartActivity<MainActivity>()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = GlanceModifier.defaultWeight()) {
            Text(
                text = "今日已赚",
                style = TextStyle(
                    color = ColorProvider(Color(0xFF1B9E5A)),
                    fontSize = 11.sp
                )
            )
            Spacer(modifier = GlanceModifier.height(2.dp))
            if (status.isWorkDay) {
                Text(
                    text = "¥${"%.2f".format(status.earned)}",
                    style = TextStyle(
                        color = ColorProvider(Color(0xFF1B9E5A)),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            } else {
                Text(
                    text = "休息日 ☀️",
                    style = TextStyle(
                        color = ColorProvider(Color(0xFF6B7280)),
                        fontSize = 16.sp
                    )
                )
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = status.statusText,
                style = TextStyle(
                    color = ColorProvider(Color(0xFF9CA3AF)),
                    fontSize = 11.sp
                )
            )
            Text(
                text = "${(status.progress * 100).toInt()}%",
                style = TextStyle(
                    color = ColorProvider(Color(0xFF4A90D9)),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

class SalaryWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = SalaryWidget()
}
