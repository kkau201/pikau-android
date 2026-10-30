package com.kaylakautai.profile

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kaylakautai.ui.components.PikauButton
import com.kaylakautai.ui.components.PikauButtonStyle
import com.kaylakautai.ui.components.PikauText
import com.kaylakautai.ui.extensions.Spacer
import com.kaylakautai.ui.layout.PikauScreenScaffold
import com.kaylakautai.ui.lce.FullScreenError
import com.kaylakautai.ui.lce.FullScreenLoader
import com.kaylakautai.ui.lce.UiState
import com.kaylakautai.ui.theme.PikauShapes
import com.kaylakautai.ui.theme.Spacing
import com.kaylakautai.ui.util.PikauPreviews
import com.kaylakautai.ui.util.PikauScreenPreviewSurface
import com.kaylakautai.pikau.ui.R as UiR

private val HeaderIconSize = 80.dp
private val RowIconSize = 26.dp

@Composable
fun ProfileScreen() {
    val vm: ProfileViewModel = hiltViewModel()
    val state by vm.uiState.collectAsStateWithLifecycle()

    when (val currentState = state) {
        is UiState.Loading -> FullScreenLoader(
            title = R.string.profile_loading_title,
            message = R.string.profile_loading_message,
        )

        is UiState.Error -> FullScreenError(currentState.error, onRetryClick = vm::onRetryClick)
        is UiState.Content -> ProfileScreenContent(
            state = currentState.data,
            onEditProfileClick = vm::onEditProfileClick,
            onLinkClick = vm::onLinkClick,
        )
    }
}

@Composable
fun ProfileScreenContent(
    state: ProfileScreenData,
    onEditProfileClick: () -> Unit,
    onLinkClick: (ProfileLink) -> Unit,
) {
    val nameStyle = MaterialTheme.typography.titleLarge.copy(
        lineHeight = 48.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Top,
            trim = LineHeightStyle.Trim.FirstLineTop,
        ),
    )

    PikauScreenScaffold(
        header = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing.large),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    val hiker = painterResource(UiR.drawable.ic_pikau)
                    Image(
                        painter = hiker,
                        contentDescription = null,
                        modifier = Modifier
                            .height(HeaderIconSize)
                            .aspectRatio(
                                hiker.intrinsicSize.width / hiker.intrinsicSize.height,
                                matchHeightConstraintsFirst = true,
                            ),
                    )
                    Spacer(Spacing.medium)
                    Column(horizontalAlignment = Alignment.Start) {
                        PikauText(
                            textRes = R.string.profile_greeting,
                            style = MaterialTheme.typography.titleSmall.copy(
                                lineHeight = 22.sp,
                                lineHeightStyle = LineHeightStyle(
                                    alignment = LineHeightStyle.Alignment.Bottom,
                                    trim = LineHeightStyle.Trim.LastLineBottom,
                                ),
                            ),
                            textAlign = TextAlign.Start,
                        )
                        PikauText(
                            textRes = R.string.profile_display_name,
                            style = nameStyle,
                            textAlign = TextAlign.Start,
                        )
                    }
                }
                PikauButton(
                    textRes = R.string.profile_edit_button,
                    onClick = onEditProfileClick,
                    modifier = Modifier.fillMaxWidth(),
                    style = PikauButtonStyle.Secondary,
                )
            }
        },
    ) {
        Column(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(Spacing.large),
            ) {
                ProfileStatsCard(state)
                profileSections.forEach { (titleRes, rows) ->
                    ProfileSection(
                        titleRes = titleRes,
                        rows = rows,
                        onLinkClick = onLinkClick,
                    )
                }
            }
            PikauText(
                textRes = R.string.profile_version,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                ),
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Spacing.small, bottom = Spacing.medium),
            )
        }
    }
}

@Composable
private fun ProfileStatsCard(state: ProfileScreenData) {
    val numberStyle = MaterialTheme.typography.titleMedium.copy(
        fontSize = 32.sp,
        lineHeight = 36.sp,
        fontWeight = FontWeight.Bold,
    )
    val labelStyle = MaterialTheme.typography.bodyMedium.copy(
        fontSize = 13.sp,
        lineHeight = 16.sp,
    )
    val onCard = MaterialTheme.colorScheme.onPrimary
    val stats = listOf(
        stringResource(R.string.profile_stat_trips_completed) to state.tripsCompleted.toString(),
        stringResource(R.string.profile_stat_total_distance) to distanceLabel(state.totalDistanceKm),
        stringResource(R.string.profile_stat_avg_pack_weight) to weightLabel(state.avgPackWeightKg),
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(PikauShapes.medium)
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = Spacing.small, vertical = Spacing.medium),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        stats.forEach { (label, value) ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                PikauText(
                    text = value,
                    style = numberStyle,
                    color = onCard,
                )
                PikauText(
                    text = label,
                    style = labelStyle,
                    color = onCard,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun ProfileSection(
    @StringRes titleRes: Int,
    rows: List<ProfileRow>,
    onLinkClick: (ProfileLink) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xSmall)) {
        PikauText(
            textRes = titleRes,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Spacing.small),
        )
        rows.forEach { row ->
            ProfileLinkRow(row = row, onClick = { onLinkClick(row.link) })
        }
    }
}

@Composable
private fun ProfileLinkRow(
    row: ProfileRow,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = Spacing.small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.medium),
    ) {
        Image(
            painter = painterResource(row.iconRes),
            contentDescription = null,
            modifier = Modifier.size(RowIconSize),
        )
        PikauText(
            textRes = row.labelRes,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun distanceLabel(km: Int?): String = km?.let {
    stringResource(R.string.profile_stat_distance_km, it)
} ?: stringResource(R.string.profile_stat_empty)

@Composable
private fun weightLabel(kg: Int?): String = kg?.let {
    stringResource(R.string.profile_stat_weight_kg, it)
} ?: stringResource(R.string.profile_stat_empty)

@PikauPreviews
@Composable
private fun ProfileScreenPreview() {
    PikauScreenPreviewSurface {
        ProfileScreenContent(
            state = ProfileScreenData(
                tripsCompleted = 1,
                totalDistanceKm = 10,
                avgPackWeightKg = 12,
            ),
            onEditProfileClick = {},
            onLinkClick = {},
        )
    }
}
