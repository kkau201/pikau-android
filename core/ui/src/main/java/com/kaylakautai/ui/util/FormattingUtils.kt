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
        // Fallback for invalid date ranges to prevent UI crashes.
        // TODO: Add non-fatal logging to track if this theoretically impossible state occurs.
        endDate.isBefore(startDate) -> startDate.format(fullFormatter)

        // Different years: "28 Dec 2026 – 2 Jan 2027"
        startDate.year != endDate.year ->
            "${startDate.format(fullFormatter)} – ${endDate.format(fullFormatter)}"

        // Different months, same year: "12 - 13 Sep 2026"
        startDate.month != endDate.month ->
            "${startDate.format(monthDayFormatter)} – ${endDate.format(fullFormatter)}"

        // Different days, same month and year: "12 - 13 Sep 2026"
        startDate.dayOfMonth != endDate.dayOfMonth ->
            "${startDate.dayOfMonth} – ${endDate.format(fullFormatter)}"

        // Exact same date: "12 Sep 2026"
        else -> endDate.format(fullFormatter)
    }
}