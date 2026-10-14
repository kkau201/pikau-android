package com.kaylakautai.ui.util

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.util.Locale

class FormattingUtilsTest {

    private lateinit var defaultLocale: Locale

    @Before
    fun setUp() {
        defaultLocale = Locale.getDefault()
        Locale.setDefault(Locale.US)
    }

    @After
    fun tearDown() {
        Locale.setDefault(defaultLocale)
    }

    @Test
    fun `formateDateRangeToDisplay - same day - returns single full formatted date`() {
        val startDate = LocalDate.of(2026, 9, 12)
        val endDate = LocalDate.of(2026, 9, 12)

        val result = formatDateRangeToDisplay(startDate, endDate)

        assertEquals("12 Sep 2026", result)
    }

    @Test
    fun `formateDateRangeToDisplay - same month and year, different days - returns day range and month year`() {
        val startDate = LocalDate.of(2026, 9, 12)
        val endDate = LocalDate.of(2026, 9, 13)

        val result = formatDateRangeToDisplay(startDate, endDate)

        assertEquals("12 – 13 Sep 2026", result)
    }

    @Test
    fun `formateDateRangeToDisplay - same year, different months - returns month-day start and full end date`() {
        val startDate = LocalDate.of(2026, 8, 28)
        val endDate = LocalDate.of(2026, 9, 2)

        val result = formatDateRangeToDisplay(startDate, endDate)

        assertEquals("28 Aug – 2 Sep 2026", result)
    }

    @Test
    fun `formateDateRangeToDisplay - different years - returns full start date and full end date`() {
        val startDate = LocalDate.of(2026, 12, 28)
        val endDate = LocalDate.of(2027, 1, 2)

        val result = formatDateRangeToDisplay(startDate, endDate)

        assertEquals("28 Dec 2026 – 2 Jan 2027", result)
    }

    @Test
    fun `formateDateRangeToDisplay - start date after end date - returns formatted start date`() {
        val startDate = LocalDate.of(2026, 9, 13)
        val endDate = LocalDate.of(2026, 9, 12)

        val result = formatDateRangeToDisplay(startDate, endDate)

        assertEquals("13 Sep 2026", result)
    }
}
