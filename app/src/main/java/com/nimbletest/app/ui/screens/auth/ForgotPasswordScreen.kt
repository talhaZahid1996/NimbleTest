package com.nimbletest.app.ui.screens.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nimbletest.app.R
import com.nimbletest.app.ui.components.ContentLoadingProgressIndicator
import com.nimbletest.app.ui.components.NimbleButton
import com.nimbletest.app.ui.components.NimbleTextField
import com.nimbletest.app.ui.screens.auth.ForgotPasswordViewModel.ForgotPasswordEvent
import com.nimbletest.app.ui.screens.auth.ForgotPasswordViewModel.ForgotPasswordInfo
import com.nimbletest.app.ui.screens.auth.ForgotPasswordViewModel.ForgotPasswordState
import com.nimbletest.app.ui.theme.LocalNimbleColors
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter") // this is to keep the image over scaffold surface
fun ForgotPasswordScreen(
    forgotPasswordInfo: ForgotPasswordInfo = ForgotPasswordInfo(),
    forgotPasswordState: ForgotPasswordState = ForgotPasswordState.Idle,
    isForgotPasswordInfoValid: Boolean = false,
    isValidEmail: Boolean = true,
    onEvent: (ForgotPasswordEvent) -> Unit = {},
    goToLoginScreen: () -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    var errorMessage by rememberSaveable { mutableStateOf("") }

    when (forgotPasswordState) {
        is ForgotPasswordState.Idle -> Unit
        is ForgotPasswordState.Loading -> {
            ContentLoadingProgressIndicator(true)
        }

        is ForgotPasswordState.Error -> {
            errorMessage = forgotPasswordState.message
        }

        is ForgotPasswordState.Success -> {
            goToLoginScreen()
        }
    }

    Scaffold(topBar = {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            IconButton(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp),
                onClick = onBackPressed
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack, contentDescription = null
                )
            }
        }
    }) {
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
                        ), alpha = 0.9f
                    )
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // logo
                item {
                    Box(
                        modifier = Modifier.size(296.dp),
                    ) {
                        Image(
                            modifier = Modifier
                                .size(296.dp)
                                .align(Alignment.TopCenter),
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentScale = ContentScale.Fit,
                            contentDescription = null
                        )
                        Text(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 64.dp),
                            text = stringResource(id = R.string.forgot_password_disclaimer_message),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                textAlign = TextAlign.Center
                            ),
                        )
                    }
                }
                item {
                    Column(
                        modifier = Modifier
                            .imePadding()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        NimbleTextField(
                            value = forgotPasswordInfo.email,
                            onValueChange = { onEvent(ForgotPasswordEvent.OnEmailChange(it)) },
                            imeAction = ImeAction.Done,
                            errorMessage = if (forgotPasswordInfo.email.isNotEmpty() && !isValidEmail) {
                                stringResource(R.string.forgot_password_please_enter_a_valid_email_error_label)
                            } else {
                                ""
                            },
                            placeHolderText = stringResource(R.string.forgot_password_email_placeholder)
                        )
                        NimbleButton(
                            modifier = Modifier.fillMaxWidth(),
                            buttonText = stringResource(R.string.forgot_password_btn_reset_label),
                            isEnabled = isForgotPasswordInfoValid,
                        ) {
                            onEvent(ForgotPasswordEvent.OnSendForgotPasswordRequest)
                        }
                    }
                }
            }
        }
    }
}