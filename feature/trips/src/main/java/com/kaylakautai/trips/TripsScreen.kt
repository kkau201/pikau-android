package com.kaylakautai.trips

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kaylakautai.ui.theme.PikauTheme

@Composable
fun TripsScreen() {
    // TODO WIP Currently a placeholder
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Trips", style = MaterialTheme.typography.titleLarge)
    }
}

@Preview
@Composable
fun PreviewTripsScreen() {
    PikauTheme {
        TripsScreen()
    }
}