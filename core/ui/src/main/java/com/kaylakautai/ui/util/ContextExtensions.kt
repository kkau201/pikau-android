package com.kaylakautai.ui.util

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun Context.openUrl(url: String) {
    if (url.isBlank()) return
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    runCatching { startActivity(intent) }
}