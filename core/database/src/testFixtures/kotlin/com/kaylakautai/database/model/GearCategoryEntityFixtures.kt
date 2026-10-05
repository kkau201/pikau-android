package com.kaylakautai.database.model

fun testGearCategoryEntity(
    id: String = "shelter",
    displayText: String = "Shelter",
    isArchived: Boolean = false
) = GearCategoryEntity(id = id, displayText = displayText, isArchived = isArchived)