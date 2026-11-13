package com.kaylakautai.profile

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import org.json.JSONArray

interface OpenSourceLibrariesSource {
    fun read(): List<OpenSourceLibrary>
}

internal class AssetOpenSourceLibrariesSource @Inject constructor(
    @ApplicationContext private val context: Context,
) : OpenSourceLibrariesSource {
    override fun read(): List<OpenSourceLibrary> {
        val json = context.assets.open(ASSET_FILE).bufferedReader().use { it.readText() }
        val array = JSONArray(json)
        return List(array.length()) { index ->
            val item = array.getJSONObject(index)
            OpenSourceLibrary(
                name = item.getString("name"),
                detail = item.getString("detail"),
                version = item.getString("version"),
                license = item.getString("license"),
                licenseUrl = item.getString("licenseUrl"),
            )
        }
    }

    private companion object {
        const val ASSET_FILE = "open_source_libraries.json"
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class OpenSourceLibrariesModule {
    @Binds
    abstract fun bindSource(impl: AssetOpenSourceLibrariesSource): OpenSourceLibrariesSource
}
