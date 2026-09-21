package com.kaylakautai.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kaylakautai.model.Difficulty
import com.kaylakautai.model.Trip
import com.kaylakautai.model.TripCategory
import java.time.LocalDate

/**
 * Defines a trip a user creates.
 */
@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val location: String,
    @ColumnInfo(name = "start_date")
    val startDate: LocalDate,
    @ColumnInfo(name = "end_date")
    val endDate: LocalDate,
    val categories: List<String>,
    val difficulty: Difficulty
)

fun TripEntity.asModel(
    categories: List<TripCategory>
) = Trip(
    id = id,
    name = name,
    location = location,
    startDate = startDate,
    endDate = endDate,
    categories = categories,
    difficulty = difficulty
)