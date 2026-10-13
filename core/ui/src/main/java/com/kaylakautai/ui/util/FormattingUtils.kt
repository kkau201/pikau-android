package com.kaylakautai.ui.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val FullDatePattern = "d MMM yyyy"
private const val MonthDayPattern = "d MMM"

/**
 * Formats a start and end date to display text
 * e.g.
 * "12 - 13 Sep 2026"
 * "28 Dec 2026 – 2 Jan 2027"
 * "12 Sep 2026"
 */
fun formatDateRangeToDisplay(
    startDate: LocalDate,
    endDate: LocalDate,
    locale: Locale = Locale.getDefault()
): String {
    val fullFormatter = DateTimeFormatter.ofPattern(FullDatePattern, locale)
    val monthDayFormatter = DateTimeFormatter.ofPattern(MonthDayPattern, locale)

    return when {
        // TODO Check if this is the behaviour I want - don't want to crash/show error
        endDate.isBefore(startDate) -> startDate.format(fullFormatter)
        startDate.year != endDate.year ->
            "${startDate.format(fullFormatter)} – ${endDate.format(fullFormatter)}"

        startDate.month != endDate.month ->
            "${startDate.format(monthDayFormatter)} – ${endDate.format(fullFormatter)}"

        startDate.dayOfMonth != endDate.dayOfMonth ->
            "${startDate.dayOfMonth} – ${endDate.format(fullFormatter)}"

        else -> endDate.format(fullFormatter)
    }
}