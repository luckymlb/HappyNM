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
import com.happynm.widget.worker.WidgetAlarmReceiver
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            MainWidgetContent()
        }
    }
}

@Composable
private fun MainWidgetContent() {
    val prefs = currentState<Preferences>()
    val settings = prefsToSettings(prefs)
    val status = SalaryCalculator.calculate(settings)
    val now = LocalDateTime.now()
    val today = now.toLocalDate()
    val progressPercent = (status.progress * 100).toInt()

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(Color.White))
            .cornerRadius(24.dp)
            .padding(horizontal = 22.dp, vertical = 16.dp)
            .clickable(actionStartActivity<MainActivity>())
    ) {
        if (status.isWorkDay) {
            // 顶部：标签 + 日薪
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "今日已赚",
                    style = TextStyle(
                        color = ColorProvider(Color(0xFF9CA3AF)),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                Spacer(modifier = GlanceModifier.defaultWeight())
                Text(
                    text = "¥${"%.0f".format(status.dailySalary)}/天",
                    style = TextStyle(
                        color = ColorProvider(Color(0xFFD1D5DB)),
                        fontSize = 11.sp
                    )
                )
            }

            Spacer(modifier = GlanceModifier.height(8.dp))

            // 核心金额
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "%.2f".format(status.earned),
                    style = TextStyle(
                        color = ColorProvider(Color(0xFF111827)),
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = GlanceModifier.defaultWeight())

            // 进度条
            Box(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .cornerRadius(2.dp)
                    .background(ColorProvider(Color(0xFFF3F4F6)))
            ) {
                Row(modifier = GlanceModifier.fillMaxWidth()) {
                    if (progressPercent > 0) {
                        Box(
                            modifier = GlanceModifier
                                .height(4.dp)
                                .width((progressPercent * 2.8).toInt().dp.coerceAtMost(280.dp))
                                .cornerRadius(2.dp)
                                .background(ColorProvider(Color(0xFF10B981)))
                        ) {}
                    }
                    Spacer(modifier = GlanceModifier.defaultWeight())
                }
            }

            Spacer(modifier = GlanceModifier.height(8.dp))

            // 底部状态行
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = status.statusText,
                    style = TextStyle(
                        color = ColorProvider(
                            if (status.isWorking) Color(0xFF10B981) else Color(0xFFD1D5DB)
                        ),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = " · ${progressPercent}%",
                    style = TextStyle(
                        color = ColorProvider(Color(0xFF9CA3AF)),
                        fontSize = 11.sp
                    )
                )
                Spacer(modifier = GlanceModifier.defaultWeight())
                Text(
                    text = now.format(DateTimeFormatter.ofPattern("HH:mm", Locale.US)),
                    style = TextStyle(
                        color = ColorProvider(Color(0xFFD1D5DB)),
                        fontSize = 11.sp
                    )
                )
            }
        } else {
            // 非工作日
            Text(
                text = "${today.format(DateTimeFormatter.ofPattern("M月d日 EEEE", Locale.CHINESE))}",
                style = TextStyle(
                    color = ColorProvider(Color(0xFFD1D5DB)),
                    fontSize = 11.sp
                )
            )
            Spacer(modifier = GlanceModifier.height(12.dp))
            Text(
                text = "休息日",
                style = TextStyle(
                    color = ColorProvider(Color(0xFF111827)),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = GlanceModifier.defaultWeight())
            Text(
                text = "好好休息，明天继续",
                style = TextStyle(
                    color = ColorProvider(Color(0xFF9CA3AF)),
                    fontSize = 11.sp
                )
            )
        }
    }
}

private fun prefsToSettings(prefs: Preferences): UserSettings {
    return UserSettings(
        monthlySalary = prefs[SettingsKeys.MONTHLY_SALARY] ?: 10000.0,
        workStartHour = prefs[SettingsKeys.WORK_START_HOUR] ?: 9,
        workStartMinute = prefs[SettingsKeys.WORK_START_MINUTE] ?: 0,
        workEndHour = prefs[SettingsKeys.WORK_END_HOUR] ?: 18,
        workEndMinute = prefs[SettingsKeys.WORK_END_MINUTE] ?: 0,
        lunchBreakMinutes = prefs[SettingsKeys.LUNCH_BREAK_MINUTES] ?: 60,
        lunchStartHour = prefs[SettingsKeys.LUNCH_START_HOUR] ?: 12,
        lunchStartMinute = prefs[SettingsKeys.LUNCH_START_MINUTE] ?: 0,
        targetDateStr = prefs[SettingsKeys.TARGET_DATE] ?: "",
        targetLabel = prefs[SettingsKeys.TARGET_LABEL] ?: "发薪日",
        weatherCity = prefs[SettingsKeys.WEATHER_CITY] ?: "北京",
        weatherApiKey = prefs[SettingsKeys.WEATHER_API_KEY] ?: "",
        workDays = prefs[SettingsKeys.WORK_DAYS]?.split(",")?.mapNotNull { it.toIntOrNull() }
            ?: listOf(1, 2, 3, 4, 5)
    )
}

class MainWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = MainWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        WidgetAlarmReceiver.schedule(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        WidgetAlarmReceiver.cancel(context)
    }
}
