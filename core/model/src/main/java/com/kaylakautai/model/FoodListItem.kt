package com.kaylakautai.model

data class FoodListItem(
    val id: String,
    val name: String,
    val isPacked: Boolean = false
)