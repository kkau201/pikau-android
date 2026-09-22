package com.kaylakautai.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kaylakautai.model.Gear
import com.kaylakautai.model.PackingListItem

@Entity(
    tableName = "packing_list_items",
    foreignKeys = [
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["id"],
            childColumns = ["trip_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("trip_id"),
        Index(value = ["trip_id", "gear_id"], unique = true)
    ]
)
data class PackingListItemEntity(
    @PrimaryKey val
    id: String,

    @ColumnInfo(name = "trip_id")
    val tripId: String,

    @ColumnInfo(name = "gear_id")
    val gearId: String,

    @ColumnInfo(name = "is_packed")
    val isPacked: Boolean = false
)

fun PackingListItemEntity.asModel(gear: Gear) = PackingListItem(
    gear = gear,
    isPacked = isPacked
)