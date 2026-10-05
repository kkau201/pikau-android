package com.kaylakautai.database.model

fun testPackingListItemEntity(
    id: String = "packing-item-1",
    tripId: String = testTripEntity().id,
    gearId: String = testGearEntity().id,
    isPacked: Boolean = false
) = PackingListItemEntity(id = id, tripId = tripId, gearId = gearId, isPacked = isPacked)