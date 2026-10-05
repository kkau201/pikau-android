package com.kaylakautai.database.model

import com.kaylakautai.model.Difficulty
import java.time.LocalDate

fun testTripEntity(
    id: String = "trip-1",
    name: String = "Test Trip",
    location: String = "Test Location",
    startDate: LocalDate = LocalDate.of(2026, 1, 1),
    endDate: LocalDate = LocalDate.of(2026, 1, 3),
    categoryIds: List<String> = listOf(testTripCategoryEntity().id),
    difficulty: Difficulty = Difficulty.EASY
) = TripEntity(
    id = id,
    name = name,
    location = location,
    startDate = startDate,
    endDate = endDate,
    categories = categoryIds,
    difficulty = difficulty
)