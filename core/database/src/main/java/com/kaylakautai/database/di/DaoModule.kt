package com.kaylakautai.database.di

import com.kaylakautai.database.PikauDatabase
import com.kaylakautai.database.dao.FoodItemDao
import com.kaylakautai.database.dao.GearCategoryDao
import com.kaylakautai.database.dao.GearDao
import com.kaylakautai.database.dao.PackingListItemDao
import com.kaylakautai.database.dao.TripCategoryDao
import com.kaylakautai.database.dao.TripDao
import com.kaylakautai.database.dao.TripNoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {
    @Provides
    fun provideFoodItemDao(
        database: PikauDatabase
    ): FoodItemDao = database.foodItemDao()

    @Provides
    fun provideGearDao(
        database: PikauDatabase
    ): GearDao = database.gearDao()

    @Provides
    fun provideGearCategoryDao(
        database: PikauDatabase
    ): GearCategoryDao = database.gearCategoryDao()

    @Provides
    fun providePackingListItemDao(
        database: PikauDatabase
    ): PackingListItemDao = database.packingListItemDao()

    @Provides
    fun provideTripDao(
        database: PikauDatabase
    ): TripDao = database.tripDao()

    @Provides
    fun provideTripCategoryDao(
        database: PikauDatabase
    ): TripCategoryDao = database.tripCategoryDao()

    @Provides
    fun provideTripNoteDao(
        database: PikauDatabase
    ): TripNoteDao = database.tripNoteDao()
}
