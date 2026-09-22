package com.kaylakautai.data.util

import com.kaylakautai.database.model.GearCategoryEntity
import com.kaylakautai.database.model.TripCategoryEntity
import com.kaylakautai.database.model.asModel
import com.kaylakautai.model.GearCategory
import com.kaylakautai.model.TripCategory

internal fun matchTripCategories(
    allCategories: List<TripCategoryEntity>,
    tripCategories: List<String>
): List<TripCategory> = if (tripCategories.isEmpty()) {
    emptyList()
} else {
    allCategories.associateBy { it.id }.let { categoryById ->
        tripCategories.mapNotNull { id -> categoryById[id]?.asModel() }
    }
}

internal fun matchGearCategory(
    allCategories: List<GearCategoryEntity>,
    gearCategoryId: String
): GearCategory? = allCategories.find { it.id == gearCategoryId }?.asModel()