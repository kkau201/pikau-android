package com.kaylakautai.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kaylakautai.model.TripNote
import java.time.LocalDateTime

@Entity(
    tableName = "trip_notes",
    foreignKeys = [
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["id"],
            childColumns = ["trip_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("trip_id")]
)
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