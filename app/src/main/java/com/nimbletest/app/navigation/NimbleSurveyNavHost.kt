package com.nimbletest.app.navigation

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.nimbletest.app.R
import com.nimbletest.app.ui.MainViewModel
import com.nimbletest.app.ui.components.CustomAlertDialog
import com.nimbletest.app.util.NavigationRoutes.AUTH_ROUTE
import com.nimbletest.app.util.popUpToTop

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NimbleSurveyNavHost(
    startDestination: String,
    authState: MainViewModel.MainActivityState,
    logout: () -> Unit = {}
) {
    val navController = rememberNavController()
    var isVisible by rememberSaveable { mutableStateOf(false) }

    // perform clean navigation to auth route when user logout
    fun navigateToRoute(route: String) {
        navController.navigate(route) {
            popUpToTop(navController)
            launchSingleTop = true
        }
    }

    // perform navigation based on the authState
    LaunchedEffect(key1 = authState) {
        if (authState is MainViewModel.MainActivityState.Unauthorized) {
            navController.navigate(AUTH_ROUTE) {
                popUpToTop(navController)
                launchSingleTop = true
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        NavHost(
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal
                    )
                ),
            navController = navController,
            startDestination = startDestination
        ) {
            authNavGraph(navController)
            homeNavGraph(navController) { isVisible = !isVisible }
        }
    }

    if (isVisible) {
        CustomAlertDialog(
            titleString = stringResource(R.string.home_screen_logout_label),
            textString = stringResource(R.string.home_screen_logout_confirmation),
            confirmButtonText = R.string.home_screen_btn_logout_label,
            onDismiss = { isVisible = false },
            onConfirmationPressed = {
                isVisible = false
                logout()
                navigateToRoute(AUTH_ROUTE)
            }
        )
    }
}