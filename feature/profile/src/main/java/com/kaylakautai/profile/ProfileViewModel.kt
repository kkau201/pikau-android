package com.kaylakautai.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaylakautai.data.repo.TripRepository
import com.kaylakautai.ui.lce.DisplayError
import com.kaylakautai.ui.lce.UiState
import com.kaylakautai.ui.lce.asUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    tripRepository: TripRepository,
    clock: Clock,
) : ViewModel() {
    private val retryTrigger = MutableStateFlow(0)

    val uiState: StateFlow<UiState<ProfileScreenData>> = retryTrigger
        .flatMapLatest {
            tripRepository.getPastTrips(LocalDate.now(clock))
                .map { trips ->
                    // TODO get other stats
                    ProfileScreenData(tripsCompleted = trips.size)
                }
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

    fun onEditProfileClick() {
        Log.i("PROFILE_SCREEN", "onEditProfileClick() triggered")
    }

    fun onLinkClick(link: ProfileLink) {
        Log.i("PROFILE_SCREEN", "onLinkClick($link) triggered")
    }
}
