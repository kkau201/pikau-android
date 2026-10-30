package com.kaylakautai.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaylakautai.ui.lce.DisplayError
import com.kaylakautai.ui.lce.UiState
import com.kaylakautai.ui.lce.asUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class OpenSourceLibrariesViewModel @Inject constructor(
    private val source: OpenSourceLibrariesSource,
) : ViewModel() {
    private val retryTrigger = MutableStateFlow(0)

    val uiState: StateFlow<UiState<OpenSourceLibrariesScreenData>> = retryTrigger
        .flatMapLatest {
            flow { emit(source.read()) }
                .map { libraries -> OpenSourceLibrariesScreenData(libraries = libraries) }
                .flowOn(Dispatchers.IO)
                .asUiState(DisplayError.Generic)
                .onStart { emit(UiState.Loading) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading,
        )

    fun onRetryClick() {
        retryTrigger.update { it + 1 }
    }
}
