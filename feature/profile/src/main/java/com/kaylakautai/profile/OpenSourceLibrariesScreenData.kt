package com.kaylakautai.profile

import androidx.compose.runtime.Immutable

@Immutable
data class OpenSourceLibrary(
    val name: String,
    val detail: String,
    val version: String,
    val license: String,
    val licenseUrl: String,
)

@Immutable
data class OpenSourceLibrariesScreenData(
    val libraries: List<OpenSourceLibrary> = emptyList(),
)
