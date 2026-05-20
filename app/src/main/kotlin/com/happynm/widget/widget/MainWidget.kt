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
import com.happynm.widget.data.model.WeatherInfo
import com.happynm.widget.data.repository.CalendarRepository
import com.happynm.widget.domain.LunarCalendar
import com.happynm.widget.domain.SalaryCalculator
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val eventCount = CalendarRepository.getTodayEventCount(context)
        provideContent {
            MainWidgetContent(eventCount)
        }
    }
}

@Composable
private fun MainWidgetContent(eventCount: Int) {
    val prefs = currentState<Preferences>()
    val settings = prefsToSettings(prefs)
    val weather = prefsToWeather(prefs)
    val status = SalaryCalculator.calculate(settings)
    val now = LocalDateTime.now()
    val today = now.toLocalDate()
    val progressPercent = (status.progress * 100).toInt()

    Row(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(Color.White))
            .cornerRadius(22.dp)
            .padding(14.dp)
            .clickable(actionStartActivity<MainActivity>())
    ) {
        // 左侧主要信息
        Column(
            modifier = GlanceModifier
                .defaultWeight()
                .fillMaxHeight()
        ) {
            // 时间
            Text(
                text = now.format(DateTimeFormatter.ofPattern("HH:mm", Locale.US)),
                style = TextStyle(
                    color = ColorProvider(Color(0xFF1A1A2E)),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            // 日期 + 农历
            Text(
                text = "${today.format(DateTimeFormatter.ofPattern("M月d日 EEEE", Locale.CHINESE))} · ${LunarCalendar.getLunarDateString(today)}",
                style = TextStyle(
                    color = ColorProvider(Color(0xFF6B7280)),
                    fontSize = 10.sp
                )
            )

            Spacer(modifier = GlanceModifier.height(8.dp))

            // 薪资
            if (status.isWorkDay) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "今日已赚",
                        style = TextStyle(
                            color = ColorProvider(Color(0xFF1B9E5A)),
                            fontSize = 10.sp
                        )
                    )
                    Spacer(modifier = GlanceModifier.width(6.dp))
                    Text(
                        text = "● ${status.statusText} ${progressPercent}%",
                        style = TextStyle(
                            color = ColorProvider(if (status.isWorking) Color(0xFF2ECC71) else Color(0xFF9CA3AF)),
                            fontSize = 9.sp
                        )
                    )
                }
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "¥",
                        style = TextStyle(
                            color = ColorProvider(Color(0xFF2ECC71)),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        text = "%.2f".format(status.earned),
                        style = TextStyle(
                            color = ColorProvider(Color(0xFF1B9E5A)),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                // 进度条
                Spacer(modifier = GlanceModifier.height(4.dp))
                Box(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .cornerRadius(2.dp)
                        .background(ColorProvider(Color(0xFFD1FAE5)))
                ) {
                    Row(modifier = GlanceModifier.fillMaxWidth()) {
                        if (progressPercent > 0) {
                            Box(
                                modifier = GlanceModifier
                                    .height(4.dp)
                                    .width((progressPercent * 2).dp.coerceAtMost(200.dp))
                                    .cornerRadius(2.dp)
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
                        fontSize = 20.sp
                    )
                )
            }
        }

        Spacer(modifier = GlanceModifier.width(10.dp))

        // 右侧 2x2 小模块网格
        Column(
            modifier = GlanceModifier.width(110.dp).fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = GlanceModifier.fillMaxWidth()) {
                // 天气
                SmallCard(
                    emoji = "☀️",
                    text = weather?.tempRangeText ?: "--°C",
                    textColor = Color(0xFF4A90D9),
                    bgColor = Color(0xFFEFF6FF),
                    modifier = GlanceModifier.defaultWeight()
                )
                Spacer(modifier = GlanceModifier.width(4.dp))
                // 倒计时
                val days = SalaryCalculator.getCountdownDays(settings.targetDate)
                SmallCard(
                    emoji = "📅",
                    text = if (days != null && days >= 0) "${days}天" else "--",
                    textColor = Color(0xFFFF6B8A),
                    bgColor = Color(0xFFFFF0F5),
                    modifier = GlanceModifier.defaultWeight()
                )
            }
            Spacer(modifier = GlanceModifier.height(4.dp))
            Row(modifier = GlanceModifier.fillMaxWidth()) {
                // 专注
                SmallCard(
                    emoji = "🎯",
                    text = "专注",
                    textColor = Color(0xFF9B59B6),
                    bgColor = Color(0xFFF5F0FF),
                    modifier = GlanceModifier.defaultWeight()
                )
                Spacer(modifier = GlanceModifier.width(4.dp))
                // 日程
                SmallCard(
                    emoji = "$eventCount",
                    text = "日程",
                    textColor = Color(0xFFF39C12),
                    bgColor = Color(0xFFFFF5EB),
                    modifier = GlanceModifier.defaultWeight(),
                    emojiIsNumber = true
                )
            }
        }
    }
}

@Composable
private fun SmallCard(
    emoji: String,
    text: String,
    textColor: Color,
    bgColor: Color,
    modifier: GlanceModifier = GlanceModifier,
    emojiIsNumber: Boolean = false
) {
    Column(
        modifier = modifier
            .background(ColorProvider(bgColor))
            .padding(6.dp)
            .cornerRadius(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = emoji,
            style = TextStyle(
                fontSize = if (emojiIsNumber) 16.sp else 14.sp,
                fontWeight = if (emojiIsNumber) FontWeight.Bold else FontWeight.Normal,
                color = if (emojiIsNumber) ColorProvider(textColor) else ColorProvider(Color(0xFF1A1A2E))
            )
        )
        Text(
            text = text,
            style = TextStyle(
                color = ColorProvider(textColor),
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        )
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

private fun prefsToWeather(prefs: Preferences): WeatherInfo? {
    return prefs[SettingsKeys.CACHED_WEATHER]?.let {
        runCatching { Json.decodeFromString<WeatherInfo>(it) }.getOrNull()
    }
}

class MainWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = MainWidget()
}
