package com.kaylakautai.data.testing

import com.kaylakautai.data.repo.CategoryRepository
import com.kaylakautai.model.GearCategory
import com.kaylakautai.model.TripCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeCategoryRepository : CategoryRepository {
    private val gearCategories = MutableStateFlow<List<GearCategory>>(emptyList())
    private val tripCategories = MutableStateFlow<List<TripCategory>>(emptyList())

    /** Thrown from [getActiveGearCategories] on the next collection. */
    var gearCategoriesFailure: Throwable? = null

    fun emitGearCategories(categories: List<GearCategory>) {
        gearCategories.value = categories
    }

    fun emitTripCategories(categories: List<TripCategory>) {
        tripCategories.value = categories
    }

    override fun getActiveGearCategories(): Flow<List<GearCategory>> =
        gearCategories.map { categories ->
            gearCategoriesFailure?.let { throw it }
            categories
        }

    override fun getActiveTripCategories(): Flow<List<TripCategory>> = tripCategories

    override suspend fun archiveGearCategory(id: String) {
        gearCategories.update { categories -> categories.filterNot { it.id == id } }
    }

    override suspend fun archiveTripCategory(id: String) {
        tripCategories.update { categories -> categories.filterNot { it.id == id } }
    }

    override suspend fun seedDefaultsIfEmpty() = Unit
}
