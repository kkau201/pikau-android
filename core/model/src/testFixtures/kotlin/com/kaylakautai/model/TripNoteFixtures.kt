package com.kaylakautai.model

import java.time.LocalDateTime

fun testTripNote(
    id: String = "note-1",
    tripId: String = testTrip().id,
    content: String = "Test note content",
    createdAt: LocalDateTime = LocalDateTime.of(2026, 1, 1, 12, 0)
) = TripNote(id = id, tripId = tripId, content = content, createdAt = createdAt)