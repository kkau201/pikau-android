package com.kaylakautai.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kaylakautai.model.TripCategory

@Entity(tableName = "trip_categories")
data class TripCategoryEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "display_text")
    val displayText: String,

    @ColumnInfo(name = "is_archived")
    val isArchived: Boolean = false
)

fun TripCategoryEntity.toDomain() = TripCategory(id = id, displayText = displayText)