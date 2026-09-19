package com.kaylakautai.model

data class Gear(
    val id: String,
    val name: String,
    val category: GearCategory,
    val weight: Int,
    // TODO Awaiting design and architecture decision
    // val quantity: Int,
    val suggestedFor: List<TripCategory> = emptyList()
)