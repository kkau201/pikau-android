package com.kaylakautai.database.util

import androidx.room.TypeConverter
import com.kaylakautai.model.TripCategory
import kotlinx.serialization.json.Json

class CategoryListConverter {
    @TypeConverter
    fun fromCategoryList(value: List<TripCategory>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toCategoryList(value: String): List<TripCategory> {
        return Json.decodeFromString(value)
    }
}
