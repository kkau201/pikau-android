package com.kaylakautai.database.model

fun testTripCategoryEntity(
    id: String = "hiking",
    displayText: String = "Hiking",
    isArchived: Boolean = false
) = TripCategoryEntity(id = id, displayText = displayText, isArchived = isArchived)