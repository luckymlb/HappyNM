package com.happynm.widget.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherInfo(
    val tempMin: Int = 0,
    val tempMax: Int = 0,
    val iconCode: String = "",
    val description: String = "",
    val lastUpdated: Long = 0
) {
    val isExpired: Boolean
        get() = System.currentTimeMillis() - lastUpdated > 2 * 60 * 60 * 1000 // 2小时

    val tempRangeText: String
        get() = "${tempMin}°C-${tempMax}°C"
}
