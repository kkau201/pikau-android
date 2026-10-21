package com.kaylakautai.gear

import androidx.annotation.StringRes
import com.kaylakautai.model.GearCategory

data class GearScreenData(
    @StringRes val title: Int = R.string.gear_title,
    val searchQuery: String = "",
    val categories: List<GearCategory> = emptyList(),
    val selectedCategoryIds: Set<String> = emptySet(),
    val hasGear: Boolean = false,
    val gearList: List<GearListItem> = emptyList(),
)

data class GearListItem(
    val id: String,
    val name: String,
    val weightGrams: Int,
)
