package com.kaylakautai.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "packing_list_items")
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