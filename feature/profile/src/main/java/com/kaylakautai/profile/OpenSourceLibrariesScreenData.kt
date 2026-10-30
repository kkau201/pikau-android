package com.kaylakautai.profile

data class OpenSourceLibrary(
    val name: String,
    val detail: String,
    val version: String,
    val license: String,
    val licenseUrl: String,
)

data class OpenSourceLibrariesScreenData(
    val libraries: List<OpenSourceLibrary> = emptyList(),
)
