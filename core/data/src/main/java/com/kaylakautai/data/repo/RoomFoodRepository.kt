package com.kaylakautai.data.repo

import com.kaylakautai.database.dao.FoodItemDao
import com.kaylakautai.database.model.FoodItemEntity
import com.kaylakautai.database.model.asModel
import com.kaylakautai.model.FoodListItem
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

internal class RoomFoodRepository @Inject constructor(
    private val foodItemDao: FoodItemDao
) : FoodRepository {

    override fun getFoodList(tripId: String): Flow<List<FoodListItem>> =
        foodItemDao.getForTrip(tripId).map { it.map { entity -> entity.asModel() } }

    override suspend fun addItem(tripId: String, name: String) {
        foodItemDao.insertItem(
            FoodItemEntity(
                id = UUID.randomUUID().toString(),
                tripId = tripId,
                name = name
            )
        )
    }

    override suspend fun setPacked(id: String, isPacked: Boolean) =
        foodItemDao.setPacked(id, isPacked)

    override suspend fun removeItem(id: String) = foodItemDao.deleteItem(id)
}