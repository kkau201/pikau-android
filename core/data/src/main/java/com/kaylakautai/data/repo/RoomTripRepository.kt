package com.kaylakautai.data.repo

import com.kaylakautai.data.util.matchTripCategories
import com.kaylakautai.database.dao.TripCategoryDao
import com.kaylakautai.database.dao.TripDao
import com.kaylakautai.database.model.asEntity
import com.kaylakautai.database.model.asModel
import com.kaylakautai.model.Trip
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

internal class RoomTripRepository @Inject constructor(
    private val tripDao: TripDao,
    private val categoryDao: TripCategoryDao
) : TripRepository {

    override fun getAllTrips(): Flow<List<Trip>> = combine(
        tripDao.getAllTripsSortedByDate(),
        categoryDao.getAll()
    ) { trips, allCategories ->
        trips.map { tripEntity ->
            tripEntity.asModel(
                categories = matchTripCategories(allCategories, tripEntity.categories)
            )
        }
    }

    override fun getTripById(id: String): Flow<Trip?> = combine(
        tripDao.getTripById(id),
        categoryDao.getAll()
    ) { tripEntity, allCategories ->
        tripEntity?.asModel(
            categories = matchTripCategories(allCategories, tripEntity.categories)
        )
    }

    override suspend fun addTrip(newTrip: Trip) {
        tripDao.insertTrip(newTrip.asEntity())
    }

    override suspend fun updateTrip(updatedTrip: Trip) {
        tripDao.updateTrip(updatedTrip.asEntity())
    }

    override suspend fun deleteTrip(tripId: String) {
        tripDao.deleteTrip(tripId)
    }

}