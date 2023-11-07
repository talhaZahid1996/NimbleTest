package com.nimbletest.app.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimbletest.app.data.NimbleAuthRepository
import com.nimbletest.app.data.NimbleSurveyRepository
import com.nimbletest.app.data.database.entities.SurveyEntity
import com.nimbletest.app.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val surveyRepository: NimbleSurveyRepository,
    private val authRepository: NimbleAuthRepository
) : ViewModel() {

    var homeScreenData by mutableStateOf(HomeScreenData())
    var homeScreenState: HomeScreenState by mutableStateOf(HomeScreenState.Loading)

    val surveys: StateFlow<List<SurveyEntity>> = surveyRepository.getSurveys()
        .onEach {
            homeScreenState = if (it.isEmpty()) {
                HomeScreenState.Empty
            } else {
                HomeScreenState.Success
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    init {
        getUserProfile()
        getSurveys()
    }

    private fun getUserProfile() {
        viewModelScope.launch {
            when (val result = authRepository.getProfile()) {
                is Result.Success -> {
                    homeScreenData = homeScreenData.copy(
                        userName = result.data?.profileAttributes?.name.orEmpty(),
                        avatarUrl = result.data?.profileAttributes?.avatarUrl.orEmpty()
                    )
                }

                else -> Unit
            }
        }
    }

    private fun getSurveys() {
        homeScreenState = HomeScreenState.Loading
        viewModelScope.launch {
            delay(1000) // this is for smoother loading animation
            when (val result = surveyRepository.getSurveysFromNetwork()) {
                is Result.Success -> homeScreenState = HomeScreenState.Success
                is Result.Error -> {
                    homeScreenState = if (surveys.value.isEmpty()) {
                        HomeScreenState.Error(result.message.orEmpty())
                    } else {
                        HomeScreenState.Success
                    }
                }

                else -> Unit
            }
        }
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.OnRefreshSurveys -> {
                getSurveys()
                getUserProfile()
            }
        }
    }

    sealed interface HomeScreenEvent {
        data object OnRefreshSurveys : HomeScreenEvent
    }

    sealed interface HomeScreenState {
        data object Loading : HomeScreenState
        data object Success : HomeScreenState
        data object Empty : HomeScreenState
        data class Error(val message: String) : HomeScreenState
    }

    data class HomeScreenData(
        val userName: String = "",
        val avatarUrl: String = "",
        //val surveys: List<Survey> = emptyList()
    )
}