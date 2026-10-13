package com.kaylakautai.ui.lce

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.kaylakautai.pikau.ui.R

data class DisplayError(
    @StringRes val title: Int,
    @StringRes val message: Int,
    @DrawableRes val icon: Int? = null
) {
    companion object {
        // TODO add more common errors e.g., no network

        val Generic = DisplayError(
            title = R.string.error_generic_title,
            message = R.string.error_generic_message,
             icon = R.drawable.ic_generic_error
        )

        val NoTripsFound = DisplayError(
            icon = R.drawable.ic_empty_trips,
            title = R.string.error_loading_trips_title,
            message = R.string.error_loading_trips_message,
        )
    }
}