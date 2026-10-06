package com.kaylakautai.model

fun testGear(
    id: String = "gear-1",
    name: String = "Test Gear",
    category: GearCategory = testGearCategory(),
    weight: Int = 500,
    suggestedFor: List<TripCategory> = listOf(testTripCategory())
) = Gear(
    id = id,
    name = name,
    category = category,
    weight = weight,
    suggestedFor = suggestedFor
)