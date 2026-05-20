package com.happynm.widget.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.*
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.*
import androidx.glance.unit.ColorProvider
import com.happynm.widget.MainActivity
import com.happynm.widget.data.datastore.SettingsKeys
import com.happynm.widget.data.model.UserSettings
import com.happynm.widget.data.model.WeatherInfo
import com.happynm.widget.data.repository.CalendarRepository
import com.happynm.widget.widget.components.*
import kotlinx.serialization.json.Json

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

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(Color.White))
            .cornerRadius(22.dp)
            .padding(14.dp)
            .clickable(actionStartActivity<MainActivity>())
    ) {
        // 第一行：时钟 + 天气
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            ClockModule(modifier = GlanceModifier.defaultWeight())
            Spacer(modifier = GlanceModifier.width(8.dp))
            WeatherModule(
                weather = weather,
                modifier = GlanceModifier.width(68.dp)
            )
        }

        Spacer(modifier = GlanceModifier.height(8.dp))

        // 第二行：薪资 + 倒计时
        Row(modifier = GlanceModifier.fillMaxWidth()) {
            SalaryModule(
                settings = settings,
                modifier = GlanceModifier.defaultWeight()
            )
            Spacer(modifier = GlanceModifier.width(8.dp))
            CountdownModule(
                settings = settings,
                modifier = GlanceModifier.width(68.dp)
            )
        }

        Spacer(modifier = GlanceModifier.defaultWeight())

        // 第三行：工作进度 + 专注 + 日程
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            WorkStatusModule(
                settings = settings,
                modifier = GlanceModifier.defaultWeight()
            )
            Spacer(modifier = GlanceModifier.width(6.dp))
            FocusModule(modifier = GlanceModifier.width(48.dp))
            Spacer(modifier = GlanceModifier.width(6.dp))
            ScheduleModule(
                eventCount = eventCount,
                modifier = GlanceModifier.width(48.dp)
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

private fun prefsToWeather(prefs: Preferences): WeatherInfo? {
    return prefs[SettingsKeys.CACHED_WEATHER]?.let {
        runCatching { Json.decodeFromString<WeatherInfo>(it) }.getOrNull()
    }
}

class MainWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = MainWidget()
}
