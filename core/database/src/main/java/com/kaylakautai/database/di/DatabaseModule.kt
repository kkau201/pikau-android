package com.kaylakautai.database.di

import android.content.Context
import androidx.room.Room
import com.kaylakautai.database.PikauDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providesPikauDatabase(
        @ApplicationContext context: Context,
    ): PikauDatabase = Room.databaseBuilder(
        context,
        PikauDatabase::class.java,
        "pikau-database",
    ).build()
}