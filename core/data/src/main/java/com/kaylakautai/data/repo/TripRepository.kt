package com.kaylakautai.data.repo

import com.kaylakautai.model.Trip
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TripRepository {

    fun getPastTrips(date: LocalDate = LocalDate.now()): Flow<List<Trip>>

    fun getUpcomingTrips(date: LocalDate = LocalDate.now()): Flow<List<Trip>>

    fun getTripById(id: String): Flow<Trip?>

    suspend fun addTrip(newTrip: Trip)

    suspend fun updateTrip(updatedTrip: Trip)

    suspend fun deleteTrip(tripId: String)

}