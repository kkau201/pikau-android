package com.kaylakautai.gear

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaylakautai.data.repo.CategoryRepository
import com.kaylakautai.data.repo.GearRepository
import com.kaylakautai.model.Gear
import com.kaylakautai.ui.lce.DisplayError
import com.kaylakautai.ui.lce.UiState
import com.kaylakautai.ui.lce.asUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class GearViewModel @Inject constructor(
    gearRepository: GearRepository,
    categoryRepository: CategoryRepository,
) : ViewModel() {
    private val selectedCategoryIds = MutableStateFlow<Set<String>>(emptySet())
    private val searchQuery = MutableStateFlow("")
    private val retryTrigger = MutableStateFlow(0)

    val uiState: StateFlow<UiState<GearScreenData>> = retryTrigger
        .flatMapLatest {
            combine(
                gearRepository.getAllGear(),
                categoryRepository.getActiveGearCategories(),
                selectedCategoryIds,
                searchQuery,
            ) { allGear, categories, selectedIds, query ->
                val categoryIds = categories.map { it.id }.toSet()
                val activeCategoryIds = selectedIds.intersect(categoryIds)
                GearScreenData(
                    searchQuery = query,
                    categories = categories,
                    selectedCategoryIds = activeCategoryIds,
                    hasGear = allGear.isNotEmpty(),
                    gearList = allGear
                        .filter { gear -> gear.matches(activeCategoryIds, query) }
                        .map { it.toListItem() }
                )
            }
                .asUiState(displayError = DisplayError.NoGearFound)
                .onStart { emit(UiState.Loading) }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )

    fun onCategoryToggled(id: String) {
        selectedCategoryIds.update { selected ->
            if (id in selected) selected - id else selected + id
        }
    }

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    fun onNewItemClick() {
        // TODO
        Log.i("GEAR_SCREEN", "onNewItemClick() triggered")
    }

    fun onRetryClick() = retryTrigger.update { it + 1 }

    private fun Gear.matches(categoryIds: Set<String>, query: String): Boolean {
        val inCategory = categoryIds.isEmpty() || category.id in categoryIds
        val matchesQuery = query.isBlank() || name.contains(query.trim(), ignoreCase = true)
        return inCategory && matchesQuery
    }

    private fun Gear.toListItem() = GearListItem(
        id = id,
        name = name,
        weightGrams = weight,
    )
}
