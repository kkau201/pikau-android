package com.kaylakautai.data.repo

import com.kaylakautai.model.Gear
import com.kaylakautai.model.PackingListItem
import kotlinx.coroutines.flow.Flow

interface PackingListRepository {

    fun getPackingList(tripId: String): Flow<List<PackingListItem>>

    suspend fun addItem(tripId: String, gear: Gear)

    suspend fun setPacked(tripId: String, gearId: String, isPacked: Boolean)

    suspend fun removeItem(tripId: String, gearId: String)

}