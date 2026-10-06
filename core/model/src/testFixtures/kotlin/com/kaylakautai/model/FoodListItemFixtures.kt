package com.kaylakautai.model

fun testFoodListItem(
    id: String = "food-item-1",
    name: String = "Test Food Item",
    isPacked: Boolean = false
) = FoodListItem(id = id, name = name, isPacked = isPacked)