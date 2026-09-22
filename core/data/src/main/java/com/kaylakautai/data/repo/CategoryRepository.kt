package com.kaylakautai.data.repo

import com.kaylakautai.model.GearCategory
import com.kaylakautai.model.TripCategory
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    fun getActiveGearCategories(): Flow<List<GearCategory>>

    fun getActiveTripCategories(): Flow<List<TripCategory>>

    suspend fun archiveGearCategory(id: String)

    suspend fun archiveTripCategory(id: String)

    suspend fun seedDefaultsIfEmpty()

}