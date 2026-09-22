package com.kaylakautai.data.repo

import com.kaylakautai.model.FoodListItem
import kotlinx.coroutines.flow.Flow

interface FoodRepository {

    fun getFoodList(tripId: String): Flow<List<FoodListItem>>

    suspend fun addItem(tripId: String, name: String)

    suspend fun setPacked(id: String, isPacked: Boolean)

    suspend fun removeItem(id: String)

}