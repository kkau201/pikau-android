package com.kaylakautai.pikau

import android.app.Application
import com.kaylakautai.data.repo.CategoryRepository
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@HiltAndroidApp
class PikauApplication : Application() {
    @Inject
    lateinit var categoryRepository: CategoryRepository
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch { categoryRepository.seedDefaultsIfEmpty() }
    }
}