package com.kaylakautai.model

fun testPackingListItem(
    gear: Gear = testGear(),
    isPacked: Boolean = false
) = PackingListItem(gear = gear, isPacked = isPacked)