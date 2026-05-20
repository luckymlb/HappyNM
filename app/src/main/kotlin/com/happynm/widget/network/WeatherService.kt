package com.happynm.widget.network

import com.happynm.widget.data.model.WeatherInfo
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*

class WeatherService {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun fetchWeather(city: String, apiKey: String): WeatherInfo? {
        if (apiKey.isBlank()) return null

        return try {
            val response: HttpResponse = client.get(
                "https://devapi.qweather.com/v7/weather/3d"
            ) {
                parameter("location", city)
                parameter("key", apiKey)
            }

            val body = response.bodyAsText()
            val jsonObj = json.parseToJsonElement(body).jsonObject

            if (jsonObj["code"]?.jsonPrimitive?.content != "200") return null

            val daily = jsonObj["daily"]?.jsonArray?.firstOrNull()?.jsonObject ?: return null

            WeatherInfo(
                tempMin = daily["tempMin"]?.jsonPrimitive?.content?.toIntOrNull() ?: 0,
                tempMax = daily["tempMax"]?.jsonPrimitive?.content?.toIntOrNull() ?: 0,
                iconCode = daily["iconDay"]?.jsonPrimitive?.content ?: "",
                description = daily["textDay"]?.jsonPrimitive?.content ?: "",
                lastUpdated = System.currentTimeMillis()
            )
        } catch (e: Exception) {
            null
        }
    }
}
