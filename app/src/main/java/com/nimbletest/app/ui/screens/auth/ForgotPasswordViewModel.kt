package com.nimbletest.app.ui.screens.auth

import android.util.Patterns
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimbletest.app.data.NimbleAuthRepository
import com.nimbletest.app.data.result.Result
import com.nimbletest.app.ui.notification.NotificationService
import com.nimbletest.app.util.isNotBlankOrEmpty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val nimbleAuthRepository: NimbleAuthRepository,
    private val notificationService: NotificationService
) : ViewModel() {

    var forgotPasswordInfo: ForgotPasswordInfo by mutableStateOf(ForgotPasswordInfo())
    var forgotPasswordState: ForgotPasswordState by mutableStateOf(ForgotPasswordState.Idle)

    val validEmail by derivedStateOf {
        forgotPasswordInfo.email.isNotBlankOrEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(forgotPasswordInfo.email).matches()
    }

    val isForgotPasswordEnabled by derivedStateOf {
        forgotPasswordInfo.email.isNotEmpty() && validEmail
    }

    private fun forgotPassword() {
        forgotPasswordState = ForgotPasswordState.Loading
        viewModelScope.launch {
            when (val result = nimbleAuthRepository.forgotPassword(forgotPasswordInfo.email)) {
                is Result.Loading -> Unit
                is Result.Success -> {
                    forgotPasswordState = ForgotPasswordState.Success
                    notificationService.showBasicNotification()
                }

                is Result.Error -> {
                    forgotPasswordState = ForgotPasswordState.Error(result.message.orEmpty())
                }
            }
        }
    }

    fun onEvent(event: ForgotPasswordEvent) {
        when (event) {
            is ForgotPasswordEvent.OnSendForgotPasswordRequest -> {
                forgotPassword()
            }

            is ForgotPasswordEvent.OnEmailChange -> {
                forgotPasswordInfo = forgotPasswordInfo.copy(email = event.email)
            }
        }
    }

    sealed interface ForgotPasswordEvent {
        data object OnSendForgotPasswordRequest : ForgotPasswordEvent
        data class OnEmailChange(val email: String) : ForgotPasswordEvent
    }

    sealed interface ForgotPasswordState {
        data object Idle : ForgotPasswordState
        data object Loading : ForgotPasswordState
        data object Success : ForgotPasswordState
        data class Error(val message: String) : ForgotPasswordState
    }

    data class ForgotPasswordInfo(
        val email: String = ""
    )
}