package com.kaylakautai.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kaylakautai.database.model.TripEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TripDao {
    @Insert
    suspend fun insertTrip(trip: TripEntity)

    @Update
    suspend fun updateTrip(trip: TripEntity)

    @Query("DELETE FROM trips WHERE id = :tripId")
    suspend fun deleteTrip(tripId: String)

    @Query("SELECT * FROM trips WHERE start_date < :now ORDER BY start_date DESC")
    fun getPastTrips(now: LocalDate): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE start_date >= :now ORDER BY start_date ASC")
    fun getUpcomingTrips(now: LocalDate): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE id = :tripId")
    fun getTripById(tripId: String): Flow<TripEntity?>
}
