package com.kaylakautai.database.model

fun testFoodItemEntity(
    id: String = "food-item-1",
    tripId: String = testTripEntity().id,
    name: String = "Test Food Item",
    isPacked: Boolean = false
) = FoodItemEntity(id = id, tripId = tripId, name = name, isPacked = isPacked)