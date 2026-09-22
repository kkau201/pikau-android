package com.kaylakautai.data.repo

import com.kaylakautai.database.dao.PackingListItemDao
import com.kaylakautai.database.model.PackingListItemEntity
import com.kaylakautai.model.Gear
import com.kaylakautai.model.PackingListItem
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.UUID

internal class RoomPackingListRepository @Inject constructor(
    private val packingListItemDao: PackingListItemDao,
    private val gearRepository: GearRepository
) : PackingListRepository {

    override fun getPackingList(tripId: String): Flow<List<PackingListItem>> = combine(
        packingListItemDao.getForTrip(tripId),
        gearRepository.getAllGear()
    ) { items, allGear ->
        val gearById = allGear.associateBy { it.id }
        items.mapNotNull { item ->
            gearById[item.gearId]?.let { gear -> PackingListItem(gear = gear, isPacked = item.isPacked) }
        }
    }

    override suspend fun addItem(tripId: String, gear: Gear) {
        packingListItemDao.insertItem(
            PackingListItemEntity(
                id = UUID.randomUUID().toString(),
                tripId = tripId,
                gearId = gear.id,
                isPacked = false
            )
        )
    }

    override suspend fun setPacked(tripId: String, gearId: String, isPacked: Boolean) =
        packingListItemDao.setPackedByGear(tripId, gearId, isPacked)

    override suspend fun removeItem(tripId: String, gearId: String) =
        packingListItemDao.deleteByGear(tripId, gearId)
}