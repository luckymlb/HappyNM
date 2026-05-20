package com.happynm.widget.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.happynm.widget.data.model.UserSettings
import com.happynm.widget.data.model.WeatherInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "happynm_settings")

object SettingsKeys {
    val MONTHLY_SALARY = doublePreferencesKey("monthly_salary")
    val WORK_START_HOUR = intPreferencesKey("work_start_hour")
    val WORK_START_MINUTE = intPreferencesKey("work_start_minute")
    val WORK_END_HOUR = intPreferencesKey("work_end_hour")
    val WORK_END_MINUTE = intPreferencesKey("work_end_minute")
    val LUNCH_BREAK_MINUTES = intPreferencesKey("lunch_break_minutes")
    val LUNCH_START_HOUR = intPreferencesKey("lunch_start_hour")
    val LUNCH_START_MINUTE = intPreferencesKey("lunch_start_minute")
    val TARGET_DATE = stringPreferencesKey("target_date")
    val TARGET_LABEL = stringPreferencesKey("target_label")
    val WEATHER_CITY = stringPreferencesKey("weather_city")
    val WEATHER_API_KEY = stringPreferencesKey("weather_api_key")
    val WORK_DAYS = stringPreferencesKey("work_days")
    val CACHED_WEATHER = stringPreferencesKey("cached_weather")
    val REFRESH_TICK = longPreferencesKey("refresh_tick")
}

class SettingsDataStore(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }

    val settingsFlow: Flow<UserSettings> = context.settingsDataStore.data.map { prefs ->
        UserSettings(
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

    val weatherFlow: Flow<WeatherInfo?> = context.settingsDataStore.data.map { prefs ->
        prefs[SettingsKeys.CACHED_WEATHER]?.let {
            runCatching { json.decodeFromString<WeatherInfo>(it) }.getOrNull()
        }
    }

    suspend fun saveSettings(settings: UserSettings) {
        context.settingsDataStore.edit { prefs ->
            prefs[SettingsKeys.MONTHLY_SALARY] = settings.monthlySalary
            prefs[SettingsKeys.WORK_START_HOUR] = settings.workStartHour
            prefs[SettingsKeys.WORK_START_MINUTE] = settings.workStartMinute
            prefs[SettingsKeys.WORK_END_HOUR] = settings.workEndHour
            prefs[SettingsKeys.WORK_END_MINUTE] = settings.workEndMinute
            prefs[SettingsKeys.LUNCH_BREAK_MINUTES] = settings.lunchBreakMinutes
            prefs[SettingsKeys.LUNCH_START_HOUR] = settings.lunchStartHour
            prefs[SettingsKeys.LUNCH_START_MINUTE] = settings.lunchStartMinute
            prefs[SettingsKeys.TARGET_DATE] = settings.targetDateStr
            prefs[SettingsKeys.TARGET_LABEL] = settings.targetLabel
            prefs[SettingsKeys.WEATHER_CITY] = settings.weatherCity
            prefs[SettingsKeys.WEATHER_API_KEY] = settings.weatherApiKey
            prefs[SettingsKeys.WORK_DAYS] = settings.workDays.joinToString(",")
        }
    }

    suspend fun saveWeather(weather: WeatherInfo) {
        context.settingsDataStore.edit { prefs ->
            prefs[SettingsKeys.CACHED_WEATHER] = json.encodeToString(WeatherInfo.serializer(), weather)
        }
    }
}
