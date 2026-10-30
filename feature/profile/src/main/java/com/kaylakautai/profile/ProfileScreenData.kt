package com.kaylakautai.profile

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ProfileScreenData(
    val tripsCompleted: Int = 0,
    val totalDistanceKm: Int? = null,
    val avgPackWeightKg: Int? = null,
)

enum class ProfileLink {
    AppSettings,
    Notifications,
    EmergencyContact,
    PrivacyPolicy,
    OpenSource,
    ContactSupport,
}

data class ProfileRow(
    val link: ProfileLink,
    @StringRes val labelRes: Int,
    @DrawableRes val iconRes: Int,
)

val profileSections = listOf(
    R.string.profile_section_profile to listOf(
        ProfileRow(
            ProfileLink.AppSettings,
            R.string.profile_link_app_settings,
            R.drawable.ic_settings,
        ),
        ProfileRow(
            ProfileLink.Notifications,
            R.string.profile_link_notifications,
            R.drawable.ic_notifications,
        ),
        ProfileRow(
            ProfileLink.EmergencyContact,
            R.string.profile_link_emergency,
            R.drawable.ic_emergency,
        ),
    ),
    R.string.profile_section_legal to listOf(
        ProfileRow(
            ProfileLink.PrivacyPolicy,
            R.string.profile_link_privacy,
            R.drawable.ic_policy,
        ),
        ProfileRow(
            ProfileLink.OpenSource,
            R.string.profile_link_open_source,
            R.drawable.ic_libraries,
        ),
        ProfileRow(
            ProfileLink.ContactSupport,
            R.string.profile_link_support,
            R.drawable.ic_contact,
        ),
    ),
)
