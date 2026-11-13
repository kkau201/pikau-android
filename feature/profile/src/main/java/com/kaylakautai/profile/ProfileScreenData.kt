package com.kaylakautai.profile

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
data class ProfileScreenData(
    val tripsCompleted: Int = 0,
    val totalDistanceKm: Int? = null,
    val avgPackWeightKg: Int? = null,
)

@Immutable
data class ProfileRow(
    val link: ProfileLink,
    @StringRes val labelRes: Int,
    @DrawableRes val iconRes: Int,
)

enum class ProfileLink {
    AppSettings,
    Notifications,
    EmergencyContact,
    PrivacyPolicy,
    OpenSource,
    ContactSupport,
}

val profileSections = listOf(
    R.string.profile_section_profile to listOf(
        ProfileRow(
            ProfileLink.AppSettings,
            R.string.profile_link_app_settings,
            R.drawable.profile_ic_settings,
        ),
        ProfileRow(
            ProfileLink.Notifications,
            R.string.profile_link_notifications,
            R.drawable.profile_ic_notifications,
        ),
        ProfileRow(
            ProfileLink.EmergencyContact,
            R.string.profile_link_emergency,
            R.drawable.profile_ic_emergency,
        ),
    ),
    R.string.profile_section_legal to listOf(
        ProfileRow(
            ProfileLink.PrivacyPolicy,
            R.string.profile_link_privacy,
            R.drawable.profile_ic_policy,
        ),
        ProfileRow(
            ProfileLink.OpenSource,
            R.string.profile_link_open_source,
            R.drawable.profile_ic_libraries,
        ),
        ProfileRow(
            ProfileLink.ContactSupport,
            R.string.profile_link_support,
            R.drawable.profile_ic_contact,
        ),
    ),
)
