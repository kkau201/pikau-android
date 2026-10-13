package com.kaylakautai.trips

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kaylakautai.ui.components.PikauButton
import com.kaylakautai.ui.components.PikauTabs
import com.kaylakautai.ui.components.PikauTripListItem
import com.kaylakautai.ui.extensions.Spacer
import com.kaylakautai.ui.lce.FullScreenError
import com.kaylakautai.ui.lce.FullScreenLoader
import com.kaylakautai.ui.lce.UiState
import com.kaylakautai.ui.model.TripListItem
import com.kaylakautai.ui.theme.PikauShapes
import com.kaylakautai.ui.theme.PikauTheme
import com.kaylakautai.ui.theme.Spacing

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
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            modifier = Modifier.matchParentSize(),
            alignment = Alignment.TopCenter,
            alpha = 0.25f,
            painter = painterResource(com.kaylakautai.pikau.ui.R.drawable.background),
            contentDescription = null // No use to screen reader
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = state.title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = Spacing.xlarge)
            )
            PikauTabs(
                optionsRes = TripOption.labels,
                selectedIndex = state.selectedOptionIndex,
                onOptionSelected = onOptionSelected
            )

            Spacer(Spacing.medium)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(PikauShapes.large)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(Spacing.xlarge),
                horizontalAlignment = Alignment.CenterHorizontally
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
        }

        PikauButton(
            textRes = R.string.trips_new_trip_button,
            onClick = onNewTripClick,
            leadingIcon = com.kaylakautai.pikau.ui.R.drawable.ic_add,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(Spacing.medium)
        )
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
        Text(
            text = stringResource(selectedOption.emptyStateTitleRes),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = Spacing.small)
        )
        Text(
            text = stringResource(selectedOption.emptyStateMessageRes),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
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

@Preview(showBackground = true)
@Composable
fun PreviewEmptyUpcomingTripsScreenContent() {
    PikauTheme {
        TripsScreenContent(
            state = TripsScreenData(),
            onOptionSelected = {},
            onTripClick = {},
            onNewTripClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEmptyPastTripsScreenContent() {
    PikauTheme {
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

@Preview(showBackground = true)
@Composable
fun PreviewPopulatedTripsScreenContent() {
    PikauTheme {
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