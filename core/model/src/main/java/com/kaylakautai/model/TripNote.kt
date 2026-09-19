package com.kaylakautai.model

import java.time.LocalDateTime

data class TripNote(
    val id: String,
    val tripId: String,
    val content: String,
    val createdAt: LocalDateTime
)