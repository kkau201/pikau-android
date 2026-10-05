package com.kaylakautai.database.model

import java.time.LocalDateTime

fun testTripNoteEntity(
    id: String = "note-1",
    tripId: String = testTripEntity().id,
    content: String = "Test note content",
    createdAt: LocalDateTime = LocalDateTime.of(2026, 1, 1, 12, 0)
) = TripNoteEntity(id = id, tripId = tripId, content = content, createdAt = createdAt)