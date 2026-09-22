package com.kaylakautai.database.seed

import com.kaylakautai.database.model.GearCategoryEntity
import com.kaylakautai.database.model.TripCategoryEntity

object DefaultCategories {
    val gear = listOf(
        GearCategoryEntity("shelter", "Shelter"),
        GearCategoryEntity("pack", "Pack"),
        GearCategoryEntity("sleep", "Sleep"),
        GearCategoryEntity("cooking", "Cooking"),
        GearCategoryEntity("navigation", "Navigation"),
        GearCategoryEntity("clothing", "Clothing"),
        GearCategoryEntity("footwear", "Footwear"),
        GearCategoryEntity("toiletries", "Toiletries"),
        GearCategoryEntity("safety", "Safety"),
        GearCategoryEntity("electronics", "Electronics"),
        GearCategoryEntity("accessories", "Accessories"),
        GearCategoryEntity("miscellaneous", "Miscellaneous")
    )
    val trip = listOf(
        TripCategoryEntity("hiking", "Hiking"),
        TripCategoryEntity("camping", "Camping"),
        TripCategoryEntity("backpacking", "Backpacking"),
        TripCategoryEntity("biking", "Biking"),
        TripCategoryEntity("skiing", "Skiing"),
        TripCategoryEntity("snowboarding", "Snowboarding"),
        TripCategoryEntity("bivouac", "Bivouac"),
        TripCategoryEntity("climbing", "Climbing"),
        TripCategoryEntity("mountaineering", "Mountaineering"),
        TripCategoryEntity("orienteering", "Orienteering"),
        TripCategoryEntity("fishing", "Fishing"),
        TripCategoryEntity("kayaking", "Kayaking"),
        TripCategoryEntity("hunting", "Hunting"),
    )
}