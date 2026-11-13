package com.kaylakautai.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kaylakautai.ui.components.PikauText
import com.kaylakautai.ui.layout.PikauScreenScaffold
import com.kaylakautai.ui.lce.FullScreenError
import com.kaylakautai.ui.lce.FullScreenLoader
import com.kaylakautai.ui.lce.UiState
import com.kaylakautai.ui.model.TripListItem
import com.kaylakautai.ui.theme.Spacing
import com.kaylakautai.ui.util.PikauPreviews
import com.kaylakautai.ui.util.PikauScreenPreviewSurface
import com.kaylakautai.pikau.ui.R as UiR

private val HeaderIconHeight = 72.dp

@Composable
fun HomeScreen(
    onTripClick: (String) -> Unit,
) {
    val vm: HomeViewModel = hiltViewModel()
    val state by vm.uiState.collectAsStateWithLifecycle()

    when (val currentState = state) {
        is UiState.Loading -> FullScreenLoader(
            title = R.string.home_loading_title,
            message = R.string.home_loading_message,
        )
        is UiState.Error -> FullScreenError(currentState.error, onRetryClick = vm::onRetryClick)
        is UiState.Content -> HomeScreenContent(
            state = currentState.data,
            onTripClick = onTripClick,
        )
    }
}

@Composable
fun HomeScreenContent(
    state: HomeScreenData,
    onTripClick: (String) -> Unit,
) = PikauScreenScaffold(
    header = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.medium),
        ) {
            val hiker = painterResource(UiR.drawable.ic_pikau)
            Image(
                painter = hiker,
                contentDescription = null,
                modifier = Modifier
                    .height(HeaderIconHeight)
                    .aspectRatio(
                        hiker.intrinsicSize.width / hiker.intrinsicSize.height,
                        matchHeightConstraintsFirst = true,
                    ),
            )
            Column {
                PikauText(
                    textRes = state.title,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Start,
                )
                PikauText(
                    textRes = state.tagline,
                    textAlign = TextAlign.Start,
                )
            }
        }
    },
) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.small)) {
        PikauText(
            textRes = R.string.home_upcoming_title,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth(),
        )
        val trip = state.upcomingTrip
        if (trip == null) {
            PikauText(
                textRes = R.string.home_no_upcoming_trip,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
            )
        } else {
            UpcomingTripText(trip = trip, onClick = { onTripClick(trip.id) })
        }
    }
}

@Composable
private fun UpcomingTripText(
    trip: TripListItem,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(Spacing.xSmall),
    ) {
        PikauText(
            text = trip.name,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth(),
        )
        PikauText(
            text = trip.dates,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth(),
        )
        PikauText(
            text = trip.location,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@PikauPreviews
@Composable
private fun PreviewEmptyHomeScreen() {
    PikauScreenPreviewSurface {
        HomeScreenContent(state = HomeScreenData(), onTripClick = {})
    }
}

@PikauPreviews
@Composable
private fun PreviewHomeScreenWithTrip() {
    PikauScreenPreviewSurface {
        HomeScreenContent(
            state = HomeScreenData(
                upcomingTrip = TripListItem(
                    id = "1",
                    name = "Mt Taranaki",
                    dates = "14 – 16 Jan 2027",
                    location = "Taranaki, NZ",
                ),
            ),
            onTripClick = {},
        )
    }
}
