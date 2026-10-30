package com.kaylakautai.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kaylakautai.ui.components.PikauChip
import com.kaylakautai.ui.components.PikauText
import com.kaylakautai.ui.extensions.Spacer
import com.kaylakautai.ui.layout.PikauScreenScaffold
import com.kaylakautai.ui.lce.FullScreenError
import com.kaylakautai.ui.lce.FullScreenLoader
import com.kaylakautai.ui.lce.UiState
import com.kaylakautai.ui.theme.Spacing
import com.kaylakautai.ui.util.PikauPreviews
import com.kaylakautai.ui.util.PikauScreenPreviewSurface
import com.kaylakautai.ui.util.openUrl

private val ChevronSize = 18.dp
private val BackTouchSize = 48.dp

@Composable
fun OpenSourceLibrariesScreen(
    onBackClick: () -> Unit,
) {
    val vm: OpenSourceLibrariesViewModel = hiltViewModel()
    val state by vm.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    when (val currentState = state) {
        is UiState.Loading -> FullScreenLoader(
            title = R.string.profile_open_source_loading_title,
            message = R.string.profile_open_source_loading_message,
        )

        is UiState.Error -> FullScreenError(currentState.error, onRetryClick = vm::onRetryClick)
        is UiState.Content -> OpenSourceLibrariesScreenContent(
            state = currentState.data,
            onBackClick = onBackClick,
            onLibraryClick = { library -> context.openUrl(library.licenseUrl) },
        )
    }
}

@Composable
fun OpenSourceLibrariesScreenContent(
    state: OpenSourceLibrariesScreenData,
    onBackClick: () -> Unit,
    onLibraryClick: (OpenSourceLibrary) -> Unit,
) {

    PikauScreenScaffold(
        header = {
            Column(Modifier.fillMaxWidth()) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(R.drawable.profile_ic_back),
                        contentDescription = stringResource(R.string.profile_open_source_back)
                    )
                }
                Spacer(Spacing.medium)
                PikauText(
                    textRes = R.string.profile_open_source_title,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.medium),
                )
                PikauText(
                    text = pluralStringResource(
                        R.plurals.profile_open_source_count,
                        state.libraries.size,
                        state.libraries.size,
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.medium),
                )
            }
        },
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = Spacing.xlarge),
        ) {
            item {
                PikauText(
                    textRes = R.string.profile_open_source_intro,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Spacing.medium),
                )
            }
            itemsIndexed(
                items = state.libraries,
                key = { _, library -> "${library.detail}@${library.version}" },
            ) { index, library ->
                OpenSourceLibraryRow(
                    library = library,
                    detailStyle = MaterialTheme.typography.labelMedium,
                    onClick = { onLibraryClick(library) },
                )
                if (index != state.libraries.lastIndex) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    )
                }
            }
        }
    }
}

@Composable
private fun OpenSourceLibraryRow(
    library: OpenSourceLibrary,
    detailStyle: TextStyle,
    onClick: () -> Unit,
) {
    val canOpen = library.licenseUrl.isNotBlank()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = canOpen, onClick = onClick)
            .padding(vertical = Spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.small),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.small)
        ) {
            PikauText(
                text = library.name,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
            )
            PikauText(
                text = library.detail,
                style = detailStyle,
                color = MaterialTheme.colorScheme.tertiary,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
            )
            LicenseBadge(library.license)
        }
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.widthIn(max = 100.dp),
        ) {
            PikauText(
                text = library.version,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        if (canOpen) {
            Image(
                painter = painterResource(R.drawable.profile_ic_chevron),
                contentDescription = null,
                modifier = Modifier.size(ChevronSize),
            )
        }
    }
}

@Composable
private fun LicenseBadge(license: String) {
    PikauChip(
        label = license,
        selected = true,
        onClick = null,
        selectedBgColour = MaterialTheme.colorScheme.tertiary,
        selectedContentColour = Color.White,
        singleLine = false,
    )
}

@PikauPreviews
@Composable
private fun OpenSourceLibrariesScreenPreview() {
    PikauScreenPreviewSurface {
        OpenSourceLibrariesScreenContent(
            state = OpenSourceLibrariesScreenData(
                libraries = listOf(
                    OpenSourceLibrary(
                        name = "Activity Compose",
                        detail = "androidx.activity:activity-compose",
                        version = "1.13.0",
                        license = "Apache 2.0",
                        licenseUrl = "https://www.apache.org/licenses/LICENSE-2.0",
                    ),
                    OpenSourceLibrary(
                        name = "Kotlin",
                        detail = "org.jetbrains.kotlin:kotlin-stdlib",
                        version = "2.4.20",
                        license = "Apache-2.0",
                        licenseUrl = "https://www.apache.org/licenses/LICENSE-2.0",
                    ),
                ),
            ),
            onBackClick = {},
            onLibraryClick = {},
        )
    }
}
