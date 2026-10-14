package com.kaylakautai.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val PikauDarkColorScheme = darkColorScheme(
    primary = ForestGreen,
    secondary = LightGreen,
    tertiary = OliveGreen,
    background = LightGreen,
    onPrimary = LightGreen,
    onSecondary = ForestGreen,
)

private val PikauLightColorScheme = lightColorScheme(
    primary = ForestGreen,
    secondary = LightGreen,
    tertiary = OliveGreen,
    background = LightGreen,
    onPrimary = LightGreen,
    onSecondary = ForestGreen,
)

@Composable
fun PikauTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+, however want to keep brand identity
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> PikauDarkColorScheme
        else -> PikauLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = PikauTypography,
        shapes = PikauShapes,
        content = content
    )
}