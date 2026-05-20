package com.happynm.widget.data.model

import kotlinx.serialization.Serializable
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@Serializable
data class UserSettings(
    val monthlySalary: Double = 10000.0,
    val workStartHour: Int = 9,
    val workStartMinute: Int = 0,
    val workEndHour: Int = 18,
    val workEndMinute: Int = 0,
    val lunchBreakMinutes: Int = 60,
    val lunchStartHour: Int = 12,
    val lunchStartMinute: Int = 0,
    val targetDateStr: String = "",
    val targetLabel: String = "发薪日",
    val weatherCity: String = "北京",
    val weatherApiKey: String = "",
    val workDays: List<Int> = listOf(1, 2, 3, 4, 5)
) {
    val workStartTime: LocalTime get() = LocalTime.of(workStartHour, workStartMinute)
    val workEndTime: LocalTime get() = LocalTime.of(workEndHour, workEndMinute)
    val lunchStartTime: LocalTime get() = LocalTime.of(lunchStartHour, lunchStartMinute)
    val lunchEndTime: LocalTime get() = lunchStartTime.plusMinutes(lunchBreakMinutes.toLong())

    val targetDate: LocalDate?
        get() = targetDateStr.takeIf { it.isNotBlank() }?.let {
            runCatching { LocalDate.parse(it) }.getOrNull()
        }

    fun isWorkDay(date: LocalDate): Boolean {
        return date.dayOfWeek.value in workDays
    }
}
