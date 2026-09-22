package com.kaylakautai.data.repo

import com.kaylakautai.database.dao.GearCategoryDao
import com.kaylakautai.database.dao.TripCategoryDao
import com.kaylakautai.database.seed.DefaultCategories
import com.kaylakautai.database.model.asModel
import com.kaylakautai.model.GearCategory
import com.kaylakautai.model.TripCategory
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class RoomCategoryRepository @Inject constructor(
    private val gearCategoryDao: GearCategoryDao,
    private val tripCategoryDao: TripCategoryDao
) : CategoryRepository {

    override fun getActiveGearCategories(): Flow<List<GearCategory>> =
        gearCategoryDao.getActive().map { it.map { entity -> entity.asModel() } }

    override fun getActiveTripCategories(): Flow<List<TripCategory>> =
        tripCategoryDao.getActive().map { it.map { entity -> entity.asModel() } }

    override suspend fun archiveGearCategory(id: String) = gearCategoryDao.archive(id)

    override suspend fun archiveTripCategory(id: String) = tripCategoryDao.archive(id)

    override suspend fun seedDefaultsIfEmpty() {
        if (gearCategoryDao.getAll().first().isEmpty()) {
            gearCategoryDao.insertAll(DefaultCategories.gear)
        }
        if (tripCategoryDao.getAll().first().isEmpty()) {
            tripCategoryDao.insertAll(DefaultCategories.trip)
        }
    }
}