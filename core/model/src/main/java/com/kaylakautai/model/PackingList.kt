package com.kaylakautai.model

data class PackingList(
    val id: String,
    val tripId: String,
    val items: List<PackingListItem> = emptyList()
)