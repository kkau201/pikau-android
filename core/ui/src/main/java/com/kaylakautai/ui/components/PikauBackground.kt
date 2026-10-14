package com.kaylakautai.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.kaylakautai.pikau.ui.R

@Composable
fun PikauBackground(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            modifier = Modifier.matchParentSize(),
            alignment = Alignment.TopCenter,
            alpha = 0.25f,
            painter = painterResource(R.drawable.background),
            contentDescription = null // not important to screen reader
        )
    }
}