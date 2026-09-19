package com.kaylakautai.model

data class FoodList(
    val id: String,
    val tripId: String,
    val items: List<FoodListItem>
)