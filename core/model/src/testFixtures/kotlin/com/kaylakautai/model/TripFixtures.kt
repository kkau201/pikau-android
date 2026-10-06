package com.kaylakautai.model

import java.time.LocalDate

fun testTrip(
    id: String = "trip-1",
    name: String = "Test Trip",
    location: String = "Test Location",
    startDate: LocalDate = LocalDate.of(2026, 1, 1),
    endDate: LocalDate = LocalDate.of(2026, 1, 3),
    categories: List<TripCategory> = listOf(testTripCategory()),
    difficulty: Difficulty = Difficulty.EASY
) = Trip(
    id = id,
    name = name,
    location = location,
    startDate = startDate,
    endDate = endDate,
    categories = categories,
    difficulty = difficulty
)