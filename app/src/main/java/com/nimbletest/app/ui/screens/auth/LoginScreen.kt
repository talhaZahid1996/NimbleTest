package com.nimbletest.app.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.nimbletest.app.R
import com.nimbletest.app.ui.components.ContentLoadingProgressIndicator
import com.nimbletest.app.ui.components.NimbleButton
import com.nimbletest.app.ui.components.NimbleTextField
import com.nimbletest.app.ui.screens.auth.LoginScreenViewModel.LoginFields
import com.nimbletest.app.ui.screens.auth.LoginScreenViewModel.LoginScreenEvent
import com.nimbletest.app.ui.screens.auth.LoginScreenViewModel.LoginState
import com.nimbletest.app.ui.theme.LocalNimbleColors
import kotlinx.coroutines.delay
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun LoginScreen(
    loginFields: LoginFields = LoginFields(),
    isLoginEnabled: Boolean = false,
    isValidEmail: Boolean = true,
    loginState: LoginState = LoginState.Idle,
    onEvent: (LoginScreenEvent) -> Unit = {},
    goToForgotPassword: () -> Unit = {},
    goToHomeScreen: () -> Unit = {},
) {
    var contentVisible by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(key1 = true) {
        delay(2000)
        contentVisible = true
    }

    when (loginState) {
        is LoginState.Idle -> Unit
        is LoginState.Loading -> {
            ContentLoadingProgressIndicator(isLoading = true)
        }

        is LoginState.Success -> {
            onEvent(LoginScreenEvent.OnLoginSuccess)
            goToHomeScreen()
        }

        is LoginState.Error -> {
            errorMessage = loginState.message
        }
    }

    Box(
        contentAlignment = Alignment.Center,
    ) {
        // background image
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.nimble_survey_bg_opacity),
            contentScale = ContentScale.FillBounds,
            contentDescription = null
        )
        // linear vertical gradient
        AnimatedVisibility(
            visible = contentVisible,
            enter = slideInVertically(
                initialOffsetY = { it }
            ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                LocalNimbleColors.current.nimbleDarkerGrey,
                                MaterialTheme.colorScheme.secondary,
                                MaterialTheme.colorScheme.secondary
                            )
                        ),
                        alpha = 0.9f
                    )
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = if (contentVisible) Arrangement.Top else Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // logo
            item {
                Image(
                    modifier = Modifier.size(296.dp),
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentScale = ContentScale.Fit,
                    contentDescription = null
                )
            }
            item {
                AnimatedVisibility(
                    visible = contentVisible,
                    enter = slideInVertically(
                        initialOffsetY = { it }
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .imePadding()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        NimbleTextField(
                            value = loginFields.email,
                            onValueChange = { onEvent(LoginScreenEvent.OnEmailChange(it)) },
                            imeAction = ImeAction.Next,
                            errorMessage = if (loginFields.email.isNotEmpty() && !isValidEmail) {
                                stringResource(R.string.login_please_enter_a_valid_email_error_label)
                            } else {
                                ""
                            },
                            placeHolderText = stringResource(R.string.login_email_placeholder)
                        )
                        NimbleTextField(
                            value = loginFields.password,
                            onValueChange = { onEvent(LoginScreenEvent.OnPasswordChange(it)) },
                            errorMessage = errorMessage,
                            placeHolderText = stringResource(R.string.login_password_placeholder),
                            isPasswordTextField = true,
                            imeAction = ImeAction.Done,
                            leadingIcon = {
                                TextButton(onClick = goToForgotPassword) {
                                    Text(
                                        text = stringResource(R.string.login_forgot_btn),
                                        color = Color.White.copy(alpha = 0.3f)
                                    )
                                }
                            }
                        )
                        NimbleButton(
                            modifier = Modifier.fillMaxWidth(),
                            buttonText = stringResource(R.string.log_in_btn),
                            isEnabled = isLoginEnabled,
                        ) {
                            onEvent(LoginScreenEvent.OnLoginClick)
                        }
                    }
                }
            }
        }
    }
}