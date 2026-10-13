package com.kaylakautai.ui.model

import androidx.annotation.DrawableRes

data class TripListItem(
    val id: String,
    val name: String,
    val dates: String,
    val location: String,
    val progress: Float = 0f,
    @DrawableRes val imageRes: Int? = null
)