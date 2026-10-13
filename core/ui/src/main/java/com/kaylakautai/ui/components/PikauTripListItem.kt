package com.kaylakautai.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kaylakautai.pikau.ui.R
import com.kaylakautai.ui.extensions.Spacer
import com.kaylakautai.ui.model.TripListItem
import com.kaylakautai.ui.theme.PikauShapes
import com.kaylakautai.ui.theme.Spacing
import com.kaylakautai.ui.util.PikauPreviewSurface
import com.kaylakautai.ui.util.PikauPreviews

private val TRIP_IMAGE_SIZE = 100.dp

@Composable
fun PikauTripListItem(
    tripDetails: TripListItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) = Row(
    modifier = modifier
        .clickable(onClick = onClick)
        .clip(PikauShapes.medium)
        .background(color = MaterialTheme.colorScheme.primary)
        .padding(Spacing.medium),
    verticalAlignment = Alignment.CenterVertically
) {
    Image(
        modifier = Modifier
            .size(TRIP_IMAGE_SIZE)
            .clip(PikauShapes.medium),
        contentScale = ContentScale.Crop,
        alignment = Alignment.Center,
        painter = painterResource(tripDetails.imageRes ?: R.drawable.background),
        contentDescription = null // No use to screen reader
    )

    Spacer(Spacing.medium)

    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.xSmall)
    ) {
        Text(
            text = tripDetails.name,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
        // TODO Add calendar icon
        Text(
            text = tripDetails.dates,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
        // TODO Add location icon
        Text(
            text = tripDetails.location,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(Spacing.xSmall) // 4 + 4 = 8
        LinearProgressIndicator(
            progress = { tripDetails.progress },
            modifier = Modifier,
            color = MaterialTheme.colorScheme.tertiary,
            trackColor = MaterialTheme.colorScheme.secondary,
            gapSize = 0.dp
        )
    }
}

@PikauPreviews
@Composable
private fun PikauTripListItemPreview() {
    PikauPreviewSurface {
        PikauTripListItem(
            tripDetails = TripListItem(
                "1",
                "Mt Taranaki",
                "12 - 13 Apr 2026",
                "Taranaki, NZ",
                0.5f
            ),
            onClick = {}
        )
    }
}