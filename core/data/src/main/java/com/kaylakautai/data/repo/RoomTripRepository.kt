package com.kaylakautai.data.repo

import com.kaylakautai.database.dao.TripCategoryDao
import com.kaylakautai.database.dao.TripDao
import com.kaylakautai.database.model.TripEntity
import com.kaylakautai.database.model.asEntity
import com.kaylakautai.database.model.asModel
import com.kaylakautai.model.Trip
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import java.time.LocalDate
import javax.inject.Inject

internal class RoomTripRepository @Inject constructor(
    private val tripDao: TripDao,
    private val categoryDao: TripCategoryDao
) : TripRepository {

    private fun Flow<List<TripEntity>>.resolveCategories(): Flow<List<Trip>> = combine(
        this,
        categoryDao.getAll()
    ) { trips, allCategories ->
        val categoryById = allCategories.associateBy { it.id }

        trips.map { tripEntity ->
            tripEntity.asModel(
                categories = tripEntity.categories.mapNotNull { categoryId ->
                    categoryById[categoryId]?.asModel()
                }
            )
        }
    }.distinctUntilChanged()

    override fun getPastTrips(date: LocalDate): Flow<List<Trip>> =
        tripDao.getPastTrips(date).resolveCategories()

    override fun getUpcomingTrips(date: LocalDate): Flow<List<Trip>> =
        tripDao.getUpcomingTrips(date).resolveCategories()

    override fun getTripById(id: String): Flow<Trip?> = combine(
        tripDao.getTripById(id),
        categoryDao.getAll()
    ) { tripEntity, allCategories ->
        tripEntity?.let { entity ->
            val categoryById = allCategories.associateBy { it.id }
            entity.asModel(
                categories = entity.categories.mapNotNull { categoryId ->
                    categoryById[categoryId]?.asModel()
                }
            )
        }
    }.distinctUntilChanged()

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