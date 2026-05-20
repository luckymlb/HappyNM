package com.happynm.widget.domain

import java.time.LocalDate
import java.time.temporal.ChronoUnit

object LunarCalendar {

    data class LunarDate(
        val year: Int,
        val month: Int,
        val day: Int,
        val isLeapMonth: Boolean
    ) {
        val monthName: String
            get() = if (isLeapMonth) "闰${MONTH_NAMES[month - 1]}" else MONTH_NAMES[month - 1]

        val dayName: String get() = DAY_NAMES[day - 1]

        override fun toString(): String = "$monthName$dayName"
    }

    private val MONTH_NAMES = arrayOf(
        "正月", "二月", "三月", "四月", "五月", "六月",
        "七月", "八月", "九月", "十月", "冬月", "腊月"
    )

    private val DAY_NAMES = arrayOf(
        "初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十",
        "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十",
        "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十"
    )

    private val HEAVENLY_STEMS = arrayOf("甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸")
    private val EARTHLY_BRANCHES = arrayOf("子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥")
    private val ANIMALS = arrayOf("鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪")

    // 1900-2100年农历数据表
    // 每个元素编码一年的农历信息：
    // - 低12位：各月大小（1为30天大月，0为29天小月）
    // - 第13-16位：闰月月份（0表示无闰月）
    // - 第17-20位：闰月大小（0为29天，1为30天）
    private val LUNAR_INFO = intArrayOf(
        0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
        0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977,
        0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970,
        0x06566, 0x0d4a0, 0x0ea50, 0x16a95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950,
        0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557,
        0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5b0, 0x14573, 0x052b0, 0x0a9a8, 0x0e950, 0x06aa0,
        0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0,
        0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b6a0, 0x195a6,
        0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570,
        0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x05ac0, 0x0ab60, 0x096d5, 0x092e0,
        0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5,
        0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930,
        0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530,
        0x05aa0, 0x076a3, 0x096d0, 0x04afb, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45,
        0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0,
        0x14b63, 0x09370, 0x049f8, 0x04970, 0x064b0, 0x168a6, 0x0ea50, 0x06aa0, 0x1a6c4, 0x0aae0,
        0x092e0, 0x0d2e3, 0x0c960, 0x0d557, 0x0d4a0, 0x0da50, 0x05d55, 0x056a0, 0x0a6d0, 0x055d4,
        0x052d0, 0x0a9b8, 0x0a950, 0x0b4a0, 0x0b6a6, 0x0ad50, 0x055a0, 0x0aba4, 0x0a5b0, 0x052b0,
        0x0b273, 0x06930, 0x07337, 0x06aa0, 0x0ad50, 0x14b55, 0x04b60, 0x0a570, 0x054e4, 0x0d160,
        0x0e968, 0x0d520, 0x0daa0, 0x16aa6, 0x056d0, 0x04ae0, 0x0a9d4, 0x0a4d0, 0x0d150, 0x0f252,
        0x0d520
    )

    private val BASE_DATE = LocalDate.of(1900, 1, 31) // 1900年正月初一

    fun getLunarDate(solarDate: LocalDate): LunarDate {
        val offset = ChronoUnit.DAYS.between(BASE_DATE, solarDate).toInt()

        if (offset < 0) return LunarDate(1900, 1, 1, false)

        var remaining = offset
        var lunarYear = 1900

        while (lunarYear < 2101) {
            val daysInYear = getLunarYearDays(lunarYear)
            if (remaining < daysInYear) break
            remaining -= daysInYear
            lunarYear++
        }

        val leapMonth = getLeapMonth(lunarYear)
        var lunarMonth = 1
        var isLeap = false

        for (m in 1..12) {
            val daysInMonth: Int
            if (leapMonth > 0 && m == leapMonth + 1 && !isLeap) {
                daysInMonth = getLeapMonthDays(lunarYear)
                isLeap = true
                lunarMonth = m - 1
            } else {
                val actualMonth = if (isLeap) m - 1 else m
                daysInMonth = getLunarMonthDays(lunarYear, actualMonth)
                lunarMonth = actualMonth
                if (isLeap && m == leapMonth + 1) {
                    // already handled
                }
            }

            if (remaining < daysInMonth) break
            remaining -= daysInMonth
        }

        // Recalculate properly
        return calculateLunarDate(offset, lunarYear)
    }

    private fun calculateLunarDate(totalOffset: Int, startYear: Int): LunarDate {
        var remaining = totalOffset
        var year = 1900

        while (year < 2101) {
            val daysInYear = getLunarYearDays(year)
            if (remaining < daysInYear) break
            remaining -= daysInYear
            year++
        }

        val leapMonth = getLeapMonth(year)
        var month = 0
        var isLeapMonth = false
        var passedLeap = false

        var i = 1
        while (i <= 13) {
            val days: Int
            if (leapMonth > 0 && i == leapMonth + 1 && !passedLeap) {
                days = getLeapMonthDays(year)
                passedLeap = true
                isLeapMonth = true
            } else {
                val actualMonth = if (passedLeap) i - 1 else i
                if (actualMonth > 12) break
                days = getLunarMonthDays(year, actualMonth)
                isLeapMonth = false
                month = actualMonth
            }

            if (remaining < days) {
                if (isLeapMonth) month = leapMonth
                break
            }
            remaining -= days
            i++
        }

        return LunarDate(year, month.coerceIn(1, 12), remaining + 1, isLeapMonth)
    }

    fun getLunarDateString(solarDate: LocalDate): String {
        val lunar = getLunarDate(solarDate)
        return lunar.toString()
    }

    fun getYearGanZhi(lunarYear: Int): String {
        val ganIndex = (lunarYear - 4) % 10
        val zhiIndex = (lunarYear - 4) % 12
        return "${HEAVENLY_STEMS[ganIndex]}${EARTHLY_BRANCHES[zhiIndex]}"
    }

    fun getAnimal(lunarYear: Int): String {
        return ANIMALS[(lunarYear - 4) % 12]
    }

    private fun getLunarYearDays(year: Int): Int {
        var total = 348
        val info = LUNAR_INFO[year - 1900]
        var mask = 0x8000
        for (i in 0 until 12) {
            if (info and mask != 0) total++
            mask = mask shr 1
        }
        return total + getLeapMonthDays(year)
    }

    private fun getLeapMonth(year: Int): Int {
        return LUNAR_INFO[year - 1900] and 0xf
    }

    private fun getLeapMonthDays(year: Int): Int {
        return if (getLeapMonth(year) > 0) {
            if (LUNAR_INFO[year - 1900] and 0x10000 != 0) 30 else 29
        } else 0
    }

    private fun getLunarMonthDays(year: Int, month: Int): Int {
        val bit = 16 - month
        return if (LUNAR_INFO[year - 1900] and (1 shl bit) != 0) 30 else 29
    }
}
