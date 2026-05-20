package com.happynm.widget.worker

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.happynm.widget.data.datastore.SettingsDataStore
import com.happynm.widget.network.WeatherService
import com.happynm.widget.widget.MainWidget
import kotlinx.coroutines.flow.first

class WeatherSyncWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val dataStore = SettingsDataStore(context)
            val settings = dataStore.settingsFlow.first()

            if (settings.weatherApiKey.isBlank()) return Result.success()

            val weather = WeatherService().fetchWeather(
                city = settings.weatherCity,
                apiKey = settings.weatherApiKey
            )

            if (weather != null) {
                dataStore.saveWeather(weather)
                MainWidget().updateAll(context)
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
