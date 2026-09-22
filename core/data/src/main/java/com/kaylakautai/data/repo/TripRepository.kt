package com.kaylakautai.data.repo

import com.kaylakautai.model.Trip
import kotlinx.coroutines.flow.Flow

interface TripRepository {

    fun getAllTrips(): Flow<List<Trip>>

    fun getTripById(id: String): Flow<Trip?>

    suspend fun addTrip(newTrip: Trip)

    suspend fun updateTrip(updatedTrip: Trip)

    suspend fun deleteTrip(tripId: String)

}