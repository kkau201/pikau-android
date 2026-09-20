package com.kaylakautai.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface Category {
    val id: String
    val displayText: String
}

@Serializable
data class TripCategory(
    override val id: String,
    override val displayText: String
) : Category

@Serializable
data class GearCategory(
    override val id: String,
    override val displayText: String
) : Category