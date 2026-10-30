package com.kaylakautai.profile

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import org.json.JSONArray

class OpenSourceLibrariesSource @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun read(): List<OpenSourceLibrary> {
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
