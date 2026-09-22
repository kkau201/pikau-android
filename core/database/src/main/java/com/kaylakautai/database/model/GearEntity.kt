package com.kaylakautai.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kaylakautai.model.Gear
import com.kaylakautai.model.GearCategory
import com.kaylakautai.model.TripCategory

@Entity(tableName = "gear")
data class GearEntity(
    @PrimaryKey
    val id: String,

    val name: String,

    @ColumnInfo(name = "category_id")
    val categoryId: String,

    val weight: Int,

    @ColumnInfo(name = "suggested_for_ids")
    val suggestedForIds: List<String>
)

fun GearEntity.asModel(
    category: GearCategory,
    suggestedFor: List<TripCategory>
) = Gear(
    id = id,
    name = name,
    category = category,
    weight = weight,
    suggestedFor = suggestedFor
)

fun Gear.asEntity() = GearEntity(
    id = id,
    name = name,
    categoryId = category.id,
    weight = weight,
    suggestedForIds = suggestedFor.map { it.id }
)
