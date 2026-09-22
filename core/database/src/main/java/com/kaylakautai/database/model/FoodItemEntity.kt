package com.kaylakautai.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kaylakautai.model.FoodListItem

@Entity(
    tableName = "food_items",
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
data class FoodItemEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "trip_id")
    val tripId: String,

    val name: String,

    @ColumnInfo(name = "is_packed")
    val isPacked: Boolean = false
)

fun FoodItemEntity.asModel() = FoodListItem(id = id, name = name, isPacked = isPacked)