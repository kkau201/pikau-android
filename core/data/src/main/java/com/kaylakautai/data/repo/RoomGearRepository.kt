package com.kaylakautai.data.repo

import android.util.Log
import com.kaylakautai.data.util.matchGearCategory
import com.kaylakautai.data.util.matchTripCategories
import com.kaylakautai.database.dao.GearCategoryDao
import com.kaylakautai.database.dao.GearDao
import com.kaylakautai.database.dao.TripCategoryDao
import com.kaylakautai.database.model.asEntity
import com.kaylakautai.database.model.asModel
import com.kaylakautai.model.Gear
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

internal class RoomGearRepository @Inject constructor(
    private val gearDao: GearDao,
    private val categoryDao: TripCategoryDao,
    private val gearCategoryDao: GearCategoryDao
) : GearRepository {

    override fun getAllGear(): Flow<List<Gear>> = combine(
        gearDao.getAllGear(),
        categoryDao.getAll(),
        gearCategoryDao.getAll()
    ) { allGear, allTripCategories, allGearCategories ->
        allGear.mapNotNull { gear ->
            matchGearCategory(allGearCategories, gear.categoryId)?.let { gearCategory ->
                gear.asModel(
                    category = gearCategory,
                    suggestedFor = matchTripCategories(allTripCategories, gear.suggestedForIds)
                )
            } ?: run {
                Log.e(
                    "RoomGearRepository",
                    "Gear ${gear.id} references missing category ${gear.categoryId}"
                )
                null
            }
        }
    }

    override fun getGearById(id: String): Flow<Gear?> = combine(
        gearDao.getGearById(id),
        categoryDao.getAll(),
        gearCategoryDao.getAll()
    ) { gearEntity, allTripCategories, allGearCategories ->
        if (gearEntity == null) return@combine null

        matchGearCategory(allGearCategories, gearEntity.categoryId)?.let { gearCategory ->
            gearEntity.asModel(
                category = gearCategory,
                suggestedFor = matchTripCategories(allTripCategories, gearEntity.suggestedForIds)
            )
        } ?: run {
            Log.e(
                "RoomGearRepository",
                "Gear ${gearEntity.id} references missing category ${gearEntity.categoryId}"
            )
            null
        }
    }

    override suspend fun addGear(gear: Gear) {
        gearDao.insertGear(gear.asEntity())
    }

    override suspend fun updateGear(gear: Gear) {
        gearDao.updateGear(gear.asEntity())
    }

    override suspend fun deleteGear(id: String) {
        gearDao.deleteGear(id)
    }
}