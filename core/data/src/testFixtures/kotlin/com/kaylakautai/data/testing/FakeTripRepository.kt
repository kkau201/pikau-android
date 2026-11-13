package com.kaylakautai.data.testing

import com.kaylakautai.data.repo.TripRepository
import com.kaylakautai.model.Trip
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * In-memory [TripRepository]. Date splits match the trip queries:
 * past trips start before the given date, upcoming trips start on or after it.
 */
class FakeTripRepository : TripRepository {
    private val trips = MutableStateFlow<List<Trip>>(emptyList())

    /** Thrown from the trip flows on the next collection. Clear it to let a retry succeed. */
    var failure: Throwable? = null

    fun emit(trips: List<Trip>) {
        this.trips.value = trips
    }

    override fun getPastTrips(date: LocalDate): Flow<List<Trip>> = trips.map { all ->
        failure?.let { throw it }
        all.filter { it.startDate.isBefore(date) }
            .sortedByDescending { it.startDate }
    }

    override fun getUpcomingTrips(date: LocalDate): Flow<List<Trip>> = trips.map { all ->
        failure?.let { throw it }
        all.filter { !it.startDate.isBefore(date) }
            .sortedBy { it.startDate }
    }

    override fun getTripById(id: String): Flow<Trip?> = trips.map { all ->
        failure?.let { throw it }
        all.find { it.id == id }
    }

    override suspend fun addTrip(newTrip: Trip) {
        trips.update { it + newTrip }
    }

    override suspend fun updateTrip(updatedTrip: Trip) {
        trips.update { current ->
            current.map { if (it.id == updatedTrip.id) updatedTrip else it }
        }
    }

    override suspend fun deleteTrip(tripId: String) {
        trips.update { current -> current.filterNot { it.id == tripId } }
    }
}
