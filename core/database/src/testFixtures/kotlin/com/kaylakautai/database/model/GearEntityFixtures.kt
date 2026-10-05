package com.kaylakautai.database.model

fun testGearEntity(
    id: String = "gear-1",
    name: String = "Test Gear",
    categoryId: String = testGearCategoryEntity().id,
    weight: Int = 500,
    suggestedForIds: List<String> = listOf(testTripCategoryEntity().id)
) = GearEntity(
    id = id,
    name = name,
    categoryId = categoryId,
    weight = weight,
    suggestedForIds = suggestedForIds
)