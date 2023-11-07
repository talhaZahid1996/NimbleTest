package com.nimbletest.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimbletest.app.data.NimbleAuthRepository
import com.nimbletest.app.data.datastore.DataStorePreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreRepository: DataStorePreferencesRepository,
    private val nimbleAuthRepository: NimbleAuthRepository
) : ViewModel() {

    val isAuthenticated: Boolean = runBlocking(Dispatchers.Default) {
        val savedLoginData = dataStoreRepository.userCredentials.first()
        savedLoginData?.accessToken != null
    }

    // listen to changes in the data store to perform actions
    val authState: StateFlow<MainActivityState> = dataStoreRepository.userCredentials.map {
        when (it?.accessToken) {
            null -> MainActivityState.Unauthorized
            "" -> MainActivityState.Unauthorized
            else -> MainActivityState.Idle
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MainActivityState.Idle
    )

    fun logout() {
        viewModelScope.launch {
            nimbleAuthRepository.logout()
        }
    }

    sealed interface MainActivityState {
        data object Idle : MainActivityState
        data object Unauthorized : MainActivityState
    }
}