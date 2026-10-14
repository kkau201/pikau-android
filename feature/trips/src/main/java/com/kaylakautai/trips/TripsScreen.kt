package com.kaylakautai.trips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kaylakautai.ui.components.PikauButton
import com.kaylakautai.ui.components.PikauTabs
import com.kaylakautai.ui.components.PikauText
import com.kaylakautai.ui.components.PikauTripListItem
import com.kaylakautai.ui.layout.PikauScreenScaffold
import com.kaylakautai.ui.lce.FullScreenError
import com.kaylakautai.ui.lce.FullScreenLoader
import com.kaylakautai.ui.lce.UiState
import com.kaylakautai.ui.model.TripListItem
import com.kaylakautai.ui.theme.Spacing
import com.kaylakautai.ui.util.PikauPreviews
import com.kaylakautai.ui.util.PikauScreenPreviewSurface

@Composable
fun TripsScreen(
    onTripClick: (String) -> Unit,
) {
    val vm: TripsViewModel = hiltViewModel()
    val state by vm.uiState.collectAsStateWithLifecycle()

    when (val currentState = state) {
        is UiState.Loading -> FullScreenLoader()
        is UiState.Error -> FullScreenError(currentState.error, onRetryClick = vm::onRetryClick)
        is UiState.Content -> {
            TripsScreenContent(
                state = currentState.data,
                onOptionSelected = vm::onOptionSelected,
                onTripClick = onTripClick,
                onNewTripClick = vm::onNewTripClick
            )
        }
    }
}

@Composable
fun TripsScreenContent(
    state: TripsScreenData,
    onOptionSelected: (Int) -> Unit,
    onTripClick: (String) -> Unit,
    onNewTripClick: () -> Unit,
) = PikauScreenScaffold(
    header = {
        PikauText(
            textRes = state.title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = Spacing.xlarge)
        )
        PikauTabs(
            optionsRes = TripOption.labels,
            selectedIndex = state.selectedOptionIndex,
            onOptionSelected = onOptionSelected,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    },
    bottomAction = {
        PikauButton(
            textRes = R.string.trips_new_trip_button,
            onClick = onNewTripClick,
            leadingIcon = com.kaylakautai.pikau.ui.R.drawable.ic_add
        )
    }
) {
    if (state.tripsList.isEmpty()) {
        EmptyTripsView(TripOption.entries[state.selectedOptionIndex])
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = Spacing.xlarge * 2)
        ) {
            items(items = state.tripsList, key = { it.id }) {
                PikauTripListItem(
                    tripDetails = it,
                    onClick = {
                        onTripClick(it.id)
                    }
                )
                Spacer(Modifier.height(Spacing.small))
            }
        }
    }
}

@Composable
fun EmptyTripsView(
    selectedOption: TripOption,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.large),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(id = com.kaylakautai.pikau.ui.R.drawable.ic_empty_trips),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = Spacing.medium)
        )
        PikauText(
            textRes = selectedOption.emptyStateTitleRes,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = Spacing.small)
        )
        PikauText(selectedOption.emptyStateMessageRes)
    }
}


/* PREVIEWS ========================= */
private val previewTrips = listOf(
    TripListItem(
        "1",
        "Mt Taranaki",
        "12 – 13 Apr 2026",
        "Taranaki, NZ",
        0.5f
    ),
    TripListItem(
        "2",
        "Tongariro Crossing",
        "2 May 2026",
        "Tongariro, NZ",
        0f
    )
)

@PikauPreviews
@Composable
fun PreviewEmptyUpcomingTripsScreenContent() {
    PikauScreenPreviewSurface {
        TripsScreenContent(
            state = TripsScreenData(),
            onOptionSelected = {},
            onTripClick = {},
            onNewTripClick = {}
        )
    }
}

@PikauPreviews
@Composable
fun PreviewEmptyPastTripsScreenContent() {
    PikauScreenPreviewSurface {
        TripsScreenContent(
            state = TripsScreenData(
                selectedOptionIndex = 1
            ),
            onOptionSelected = {},
            onTripClick = {},
            onNewTripClick = {}
        )
    }
}

@PikauPreviews
@Composable
fun PreviewPopulatedTripsScreenContent() {
    PikauScreenPreviewSurface {
        TripsScreenContent(
            state = TripsScreenData(
                upcomingTrips = previewTrips
            ),
            onOptionSelected = {},
            onTripClick = {},
            onNewTripClick = {},
        )
    }
}