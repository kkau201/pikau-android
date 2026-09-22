package com.kaylakautai.data.di

import com.kaylakautai.data.repo.CategoryRepository
import com.kaylakautai.data.repo.FoodRepository
import com.kaylakautai.data.repo.GearRepository
import com.kaylakautai.data.repo.PackingListRepository
import com.kaylakautai.data.repo.RoomCategoryRepository
import com.kaylakautai.data.repo.RoomFoodRepository
import com.kaylakautai.data.repo.RoomGearRepository
import com.kaylakautai.data.repo.RoomPackingListRepository
import com.kaylakautai.data.repo.RoomTripNoteRepository
import com.kaylakautai.data.repo.RoomTripRepository
import com.kaylakautai.data.repo.TripNoteRepository
import com.kaylakautai.data.repo.TripRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    abstract fun bindTripRepository(impl: RoomTripRepository): TripRepository

    @Binds
    abstract fun bindGearRepository(impl: RoomGearRepository): GearRepository

    @Binds
    abstract fun bindCategoryRepository(impl: RoomCategoryRepository): CategoryRepository

    @Binds
    abstract fun bindPackingListRepository(impl: RoomPackingListRepository): PackingListRepository

    @Binds
    abstract fun bindFoodRepository(impl: RoomFoodRepository): FoodRepository

    @Binds
    abstract fun bindTripNoteRepository(impl: RoomTripNoteRepository): TripNoteRepository
}