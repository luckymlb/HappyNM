package com.happynm.widget.domain

import com.happynm.widget.data.model.UserSettings
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object SalaryCalculator {

    data class SalaryStatus(
        val earned: Double,
        val dailySalary: Double,
        val progress: Float,
        val isWorkDay: Boolean,
        val isWorking: Boolean,
        val statusText: String
    )

    fun calculate(settings: UserSettings, now: LocalDateTime = LocalDateTime.now()): SalaryStatus {
        val today = now.toLocalDate()

        if (!settings.isWorkDay(today)) {
            return SalaryStatus(
                earned = 0.0,
                dailySalary = 0.0,
                progress = 0f,
                isWorkDay = false,
                isWorking = false,
                statusText = "今日休息"
            )
        }

        val dailySalary = getDailySalary(settings, today)
        val currentTime = now.toLocalTime()
        val workStart = settings.workStartTime
        val workEnd = settings.workEndTime

        if (currentTime.isBefore(workStart)) {
            return SalaryStatus(
                earned = 0.0,
                dailySalary = dailySalary,
                progress = 0f,
                isWorkDay = true,
                isWorking = false,
                statusText = "未开始"
            )
        }

        if (currentTime.isAfter(workEnd)) {
            return SalaryStatus(
                earned = dailySalary,
                dailySalary = dailySalary,
                progress = 1f,
                isWorkDay = true,
                isWorking = false,
                statusText = "已下班"
            )
        }

        val totalWorkMinutes = getTotalWorkMinutes(settings)
        val effectiveMinutes = getEffectiveWorkedMinutes(currentTime, settings)
        val progress = (effectiveMinutes.toFloat() / totalWorkMinutes).coerceIn(0f, 1f)
        val earned = dailySalary * progress

        return SalaryStatus(
            earned = earned,
            dailySalary = dailySalary,
            progress = progress,
            isWorkDay = true,
            isWorking = true,
            statusText = "工作中"
        )
    }

    fun getDailySalary(settings: UserSettings, date: LocalDate = LocalDate.now()): Double {
        val workDaysInMonth = getWorkDaysInMonth(date, settings)
        return if (workDaysInMonth > 0) settings.monthlySalary / workDaysInMonth else 0.0
    }

    fun getTotalWorkMinutes(settings: UserSettings): Int {
        val totalMinutes = Duration.between(settings.workStartTime, settings.workEndTime).toMinutes().toInt()
        return totalMinutes - settings.lunchBreakMinutes
    }

    private fun getEffectiveWorkedMinutes(currentTime: LocalTime, settings: UserSettings): Int {
        val minutesSinceStart = Duration.between(settings.workStartTime, currentTime).toMinutes().toInt()
        val lunchStart = settings.lunchStartTime
        val lunchEnd = settings.lunchEndTime

        val lunchDeduction = when {
            currentTime.isBefore(lunchStart) -> 0
            currentTime.isAfter(lunchEnd) -> settings.lunchBreakMinutes
            else -> Duration.between(lunchStart, currentTime).toMinutes().toInt()
        }

        return (minutesSinceStart - lunchDeduction).coerceAtLeast(0)
    }

    fun getWorkDaysInMonth(date: LocalDate, settings: UserSettings): Int {
        val firstDay = date.withDayOfMonth(1)
        val daysInMonth = date.lengthOfMonth()
        return (0 until daysInMonth).count { dayOffset ->
            settings.isWorkDay(firstDay.plusDays(dayOffset.toLong()))
        }
    }

    fun getCountdownDays(targetDate: LocalDate?, now: LocalDate = LocalDate.now()): Long? {
        return targetDate?.let { ChronoUnit.DAYS.between(now, it) }
    }

    fun formatMoney(amount: Double): String {
        return "¥%.2f".format(amount)
    }
}
