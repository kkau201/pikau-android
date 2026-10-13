package com.kaylakautai.ui.lce

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kaylakautai.ui.util.PikauPreviewSurface
import com.kaylakautai.ui.util.PikauPreviews

@Composable
fun FullScreenLoader() {
    // TODO Replace once designs are finalised
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Loading...",
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@PikauPreviews
@Composable
private fun FullScreenLoaderPreview() {
    PikauPreviewSurface {
        FullScreenLoader()
    }
}