package com.kaylakautai.ui.lce

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Content<T>(val data: T) : UiState<T>
    data class Error(val error: DisplayError, val cause: Throwable? = null) : UiState<Nothing>
}

/**
 * Converts a [Flow] of [T] into a [Flow] of [UiState].
 * Wraps emitted items in [UiState.Content] and catches exceptions to emit [UiState.Error].
 */
fun <T> Flow<T>.asUiState(
    displayError: DisplayError
): Flow<UiState<T>> = this
    .map<T, UiState<T>> { UiState.Content(it) }
    .catch { cause ->
        Log.e("asUiState", "Flow failed", cause)
        emit(UiState.Error(displayError, cause))
    }
