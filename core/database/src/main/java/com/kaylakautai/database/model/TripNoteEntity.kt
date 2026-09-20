package com.kaylakautai.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kaylakautai.model.TripNote
import java.time.LocalDateTime

@Entity(tableName = "trip_notes")
data class TripNoteEntity(
    @PrimaryKey val id: String,

    @ColumnInfo(name = "trip_id")
    val tripId: String,

    val content: String,

    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime
)

fun TripNoteEntity.asModel() = TripNote(
    id = id,
    tripId = tripId,
    content = content,
    createdAt = createdAt
)