package com.kaylakautai.data.di

import com.kaylakautai.data.util.AndroidPikauLogger
import com.kaylakautai.data.util.PikauLogger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LoggerModule {
    @Binds
    @Singleton
    abstract fun bindPikauLogger(impl: AndroidPikauLogger): PikauLogger
}
