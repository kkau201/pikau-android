package com.kaylakautai.model

import java.time.LocalDate

data class Trip(
    val id: String,
    val name: String,
    val location: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val categories: List<TripCategory>,
    val difficulty: Difficulty
) {
    val isPast: Boolean
        get() = LocalDate.now().isAfter(endDate)
}

