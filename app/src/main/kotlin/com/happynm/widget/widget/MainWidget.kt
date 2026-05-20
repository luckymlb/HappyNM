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
import com.happynm.widget.domain.LunarCalendar
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
            .cornerRadius(22.dp)
            .padding(horizontal = 18.dp, vertical = 14.dp)
            .clickable(actionStartActivity<MainActivity>())
    ) {
        // 顶部：时间 + 日薪
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = now.format(DateTimeFormatter.ofPattern("HH:mm", Locale.US)),
                style = TextStyle(
                    color = ColorProvider(Color(0xFF2D3436)),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            )
            Spacer(modifier = GlanceModifier.width(6.dp))
            Text(
                text = "${today.format(DateTimeFormatter.ofPattern("M/d EEE", Locale.CHINESE))} · ${LunarCalendar.getLunarDateString(today)}",
                style = TextStyle(
                    color = ColorProvider(Color(0xFFB2BEC3)),
                    fontSize = 10.sp
                )
            )
            Spacer(modifier = GlanceModifier.defaultWeight())
            if (status.isWorkDay && status.dailySalary > 0) {
                Box(
                    modifier = GlanceModifier
                        .background(ColorProvider(Color(0xFFF0FFF4)))
                        .cornerRadius(6.dp)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "日薪 ¥${"%.0f".format(status.dailySalary)}",
                        style = TextStyle(
                            color = ColorProvider(Color(0xFF00B894)),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }

        Spacer(modifier = GlanceModifier.height(6.dp))

        if (status.isWorkDay) {
            // 标签
            Text(
                text = "今日已赚",
                style = TextStyle(
                    color = ColorProvider(Color(0xFF636E72)),
                    fontSize = 11.sp
                )
            )
            // 大金额
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "¥",
                    style = TextStyle(
                        color = ColorProvider(Color(0xFF00B894)),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                Spacer(modifier = GlanceModifier.width(2.dp))
                Text(
                    text = "%.2f".format(status.earned),
                    style = TextStyle(
                        color = ColorProvider(Color(0xFF2D3436)),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = GlanceModifier.defaultWeight())

            // 底部：进度信息
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = GlanceModifier
                        .background(ColorProvider(
                            if (status.isWorking) Color(0xFFE8F8F5) else Color(0xFFF5F6FA)
                        ))
                        .cornerRadius(4.dp)
                        .padding(horizontal = 5.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = "● ${status.statusText}",
                        style = TextStyle(
                            color = ColorProvider(
                                if (status.isWorking) Color(0xFF00B894) else Color(0xFFB2BEC3)
                            ),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
                Spacer(modifier = GlanceModifier.defaultWeight())
                Text(
                    text = "${settings.workStartHour}:${"%02d".format(settings.workStartMinute)}–${settings.workEndHour}:${"%02d".format(settings.workEndMinute)}",
                    style = TextStyle(
                        color = ColorProvider(Color(0xFFDFE6E9)),
                        fontSize = 9.sp
                    )
                )
                Spacer(modifier = GlanceModifier.width(8.dp))
                Text(
                    text = "${progressPercent}%",
                    style = TextStyle(
                        color = ColorProvider(Color(0xFF00B894)),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Spacer(modifier = GlanceModifier.height(5.dp))
            // 进度条
            Box(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .cornerRadius(3.dp)
                    .background(ColorProvider(Color(0xFFE8F8F5)))
            ) {
                Row(modifier = GlanceModifier.fillMaxWidth()) {
                    if (progressPercent > 0) {
                        Box(
                            modifier = GlanceModifier
                                .height(5.dp)
                                .width((progressPercent * 2.6).toInt().dp.coerceAtMost(260.dp))
                                .cornerRadius(3.dp)
                                .background(ColorProvider(Color(0xFF00B894)))
                        ) {}
                    }
                    Spacer(modifier = GlanceModifier.defaultWeight())
                }
            }
        } else {
            Spacer(modifier = GlanceModifier.height(6.dp))
            Text(
                text = "今天不用上班",
                style = TextStyle(
                    color = ColorProvider(Color(0xFFB2BEC3)),
                    fontSize = 11.sp
                )
            )
            Spacer(modifier = GlanceModifier.height(2.dp))
            Text(
                text = "☀️ 好好休息",
                style = TextStyle(
                    color = ColorProvider(Color(0xFF2D3436)),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = GlanceModifier.defaultWeight())
            Text(
                text = "下个工作日继续搞钱 💰",
                style = TextStyle(
                    color = ColorProvider(Color(0xFFB2BEC3)),
                    fontSize = 10.sp
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
