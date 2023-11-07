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
import com.nimbletest.app.util.isNotBlankOrEmpty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val nimbleAuthRepository: NimbleAuthRepository
) : ViewModel() {

    var loginFields by mutableStateOf(LoginFields())
    var loginState: LoginState by mutableStateOf(LoginState.Idle)

    val validEmail by derivedStateOf {
        loginFields.email.isNotBlankOrEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(loginFields.email).matches()
    }

    val enableLogin by derivedStateOf {
        loginFields.email.isNotBlankOrEmpty() &&
                validEmail &&
                loginFields.password.isNotBlankOrEmpty()
    }

    private fun login() {
        loginState = LoginState.Loading
        viewModelScope.launch {
            val result = nimbleAuthRepository.login(
                loginFields.email,
                loginFields.password
            )
            when (result) {
                Result.Loading -> Unit
                is Result.Error -> {
                    loginState = LoginState.Error(result.data?.errors?.first()?.detail.orEmpty())
                }

                is Result.Success -> {
                    loginState = LoginState.Success
                }
            }
        }
    }

    fun onEvent(event: LoginScreenEvent) {
        when (event) {
            is LoginScreenEvent.OnLoginClick -> login()
            is LoginScreenEvent.OnLoginSuccess -> {
                loginState = LoginState.Idle
            }

            is LoginScreenEvent.OnEmailChange -> {
                loginFields = loginFields.copy(email = event.email)
            }

            is LoginScreenEvent.OnPasswordChange -> {
                loginFields = loginFields.copy(password = event.password)
            }
        }
    }

    sealed interface LoginScreenEvent {
        data object OnLoginClick : LoginScreenEvent
        data object OnLoginSuccess : LoginScreenEvent
        data class OnEmailChange(val email: String) : LoginScreenEvent
        data class OnPasswordChange(val password: String) : LoginScreenEvent
    }

    sealed interface LoginState {
        data object Idle : LoginState
        data object Loading : LoginState
        data object Success : LoginState
        data class Error(val message: String) : LoginState
    }

    data class LoginFields(
        val email: String = "",
        val password: String = ""
    )
}