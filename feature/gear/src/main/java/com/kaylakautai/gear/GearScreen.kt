package com.kaylakautai.gear

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kaylakautai.model.GearCategory
import com.kaylakautai.ui.components.PikauButton
import com.kaylakautai.ui.components.PikauChip
import com.kaylakautai.ui.components.PikauChipSize
import com.kaylakautai.ui.components.PikauInput
import com.kaylakautai.ui.components.PikauText
import com.kaylakautai.ui.layout.PikauScreenScaffold
import com.kaylakautai.ui.lce.FullScreenError
import com.kaylakautai.ui.lce.FullScreenLoader
import com.kaylakautai.ui.lce.UiState
import com.kaylakautai.ui.theme.Spacing
import com.kaylakautai.ui.util.PikauPreviews
import com.kaylakautai.ui.util.PikauScreenPreviewSurface

private val EmptyPackIconSize = 125.dp
private val ChipEdgeFadeWidth = Spacing.xlarge

@Composable
fun GearScreen(
    onGearClick: (String) -> Unit,
) {
    val vm: GearViewModel = hiltViewModel()
    val state by vm.uiState.collectAsStateWithLifecycle()

    when (val currentState = state) {
        is UiState.Loading -> FullScreenLoader(
            title = R.string.gear_loading_title,
            message = R.string.gear_loading_message,
        )

        is UiState.Error -> FullScreenError(currentState.error, onRetryClick = vm::onRetryClick)
        is UiState.Content -> {
            GearScreenContent(
                state = currentState.data,
                onSearchQueryChange = vm::onSearchQueryChange,
                onCategoryToggled = vm::onCategoryToggled,
                onGearClick = onGearClick,
                onNewItemClick = vm::onNewItemClick,
            )
        }
    }
}

@Composable
fun GearScreenContent(
    state: GearScreenData,
    onSearchQueryChange: (String) -> Unit,
    onCategoryToggled: (String) -> Unit,
    onGearClick: (String) -> Unit,
    onNewItemClick: () -> Unit,
) = PikauScreenScaffold(
    header = {
        PikauText(
            textRes = state.title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = Spacing.xlarge)
        )
        PikauInput(
            value = state.searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = stringResource(R.string.gear_search_placeholder),
            leadingIcon = com.kaylakautai.pikau.ui.R.drawable.ic_search,
            modifier = Modifier.fillMaxWidth()
        )
    },
    bottomAction = {
        PikauButton(
            textRes = R.string.gear_new_item_button,
            onClick = onNewItemClick,
            leadingIcon = com.kaylakautai.pikau.ui.R.drawable.ic_add
        )
    }
) {
    Column(modifier = Modifier.fillMaxSize()) {
        GearCategoryChips(
            categories = state.categories,
            selectedCategoryIds = state.selectedCategoryIds,
            onCategoryToggled = onCategoryToggled,
        )
        if (state.gearList.isEmpty()) {
            EmptyGearView(
                titleRes = if (state.hasGear) {
                    R.string.gear_no_results_title
                } else {
                    R.string.gear_empty_title
                },
                messageRes = if (state.hasGear) {
                    R.string.gear_no_results_message
                } else {
                    R.string.gear_empty_message
                },
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(top = Spacing.medium, bottom = Spacing.xlarge * 2)
            ) {
                items(items = state.gearList, key = { it.id }) { item ->
                    GearListItem(
                        item = item,
                        onClick = { onGearClick(item.id) }
                    )
                    Spacer(Modifier.height(Spacing.small))
                }
            }
        }
    }
}

@Composable
private fun GearCategoryChips(
    categories: List<GearCategory>,
    selectedCategoryIds: Set<String>,
    onCategoryToggled: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    val listState = rememberLazyListState()
    val canScrollBackward by remember { derivedStateOf { listState.canScrollBackward } }
    val canScrollForward by remember { derivedStateOf { listState.canScrollForward } }
    val fadeColor = colors.background

    Box(
        modifier = modifier
            .fillMaxWidth()
            .drawWithContent {
                drawContent()
                val fadeWidth = ChipEdgeFadeWidth.toPx()
                if (canScrollBackward) {
                    drawRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(fadeColor, fadeColor.copy(alpha = 0f)),
                            startX = 0f,
                            endX = fadeWidth,
                        ),
                        size = Size(fadeWidth, size.height),
                    )
                }
                if (canScrollForward) {
                    drawRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(fadeColor.copy(alpha = 0f), fadeColor),
                            startX = size.width - fadeWidth,
                            endX = size.width,
                        ),
                        topLeft = Offset(size.width - fadeWidth, 0f),
                        size = Size(fadeWidth, size.height),
                    )
                }
            }
    ) {
        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.small),
        ) {
            items(items = categories, key = { it.id }) { category ->
                PikauChip(
                    label = category.displayText,
                    selected = category.id in selectedCategoryIds,
                    onClick = { onCategoryToggled(category.id) },
                    size = PikauChipSize.Large,
                    selectedContentColour = colors.onPrimary,
                    selectedBgColour = colors.primary,
                    unselectedContentColour = colors.onTertiary,
                    unselectedBgColour = colors.tertiary
                )
            }
        }
    }
}

@Composable
private fun EmptyGearView(
    @StringRes titleRes: Int,
    @StringRes messageRes: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.large),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = com.kaylakautai.pikau.ui.R.drawable.ic_empty_pack),
            contentDescription = null,
            modifier = Modifier
                .padding(bottom = Spacing.medium)
                .size(EmptyPackIconSize)
        )
        PikauText(
            textRes = titleRes,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = Spacing.small)
        )
        PikauText(messageRes)
    }
}

@Composable
private fun GearListItem(
    item: GearListItem,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = Spacing.small),
        horizontalAlignment = Alignment.Start,
    ) {
        PikauText(
            text = item.name,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth(),
        )
        PikauText(
            text = stringResource(R.string.gear_weight_grams, item.weightGrams),
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/* PREVIEWS ========================= */
private val previewCategories = listOf(
    GearCategory(id = "clothing", displayText = "Clothing"),
    GearCategory(id = "footwear", displayText = "Footwear"),
    GearCategory(id = "safety", displayText = "Safety"),
    GearCategory(id = "toiletries", displayText = "Toiletries"),
)

private val previewGear = listOf(
    GearListItem(id = "1", name = "Rain jacket", weightGrams = 320),
    GearListItem(id = "2", name = "Merino shirt", weightGrams = 180),
)

@PikauPreviews
@Composable
private fun PreviewEmptyGearScreenContent() {
    PikauScreenPreviewSurface {
        GearScreenContent(
            state = GearScreenData(
                categories = previewCategories,
                selectedCategoryIds = setOf(previewCategories.first().id),
            ),
            onSearchQueryChange = {},
            onCategoryToggled = {},
            onGearClick = {},
            onNewItemClick = {},
        )
    }
}

@PikauPreviews
@Composable
private fun PreviewPopulatedGearScreenContent() {
    PikauScreenPreviewSurface {
        GearScreenContent(
            state = GearScreenData(
                categories = previewCategories,
                selectedCategoryIds = setOf(
                    previewCategories[0].id,
                    previewCategories[2].id,
                ),
                hasGear = true,
                gearList = previewGear,
            ),
            onSearchQueryChange = {},
            onCategoryToggled = {},
            onGearClick = {},
            onNewItemClick = {},
        )
    }
}
