package com.kaylakautai.model

interface Category {
    val id: String
    val displayText: String
}

data class TripCategory(
    override val id: String,
    override val displayText: String
): Category

data class GearCategory(
    override val id: String,
    override val displayText: String
): Category