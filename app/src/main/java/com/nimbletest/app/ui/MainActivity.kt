package com.nimbletest.app.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.nimbletest.app.navigation.NimbleSurveyNavHost
import com.nimbletest.app.ui.MainViewModel.MainActivityState
import com.nimbletest.app.ui.theme.NimbleSurveyTheme
import com.nimbletest.app.util.NavigationRoutes.AUTH_ROUTE
import com.nimbletest.app.util.NavigationRoutes.HOME_ROUTE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        var authState: MainActivityState by mutableStateOf(MainActivityState.Idle)

        // Update the ui when the authState changes
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authState.onEach { authState = it }.collect()
            }
        }

        // Turn off the decor fitting system windows, which allows us to handle insets,
        // including IME animations
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Load the startDestination route before displaying any content, so we can navigate directly to the
        // appropriate screen without flashing the UI at all.
        val startDestination = if (viewModel.isAuthenticated) {
            HOME_ROUTE
        } else {
            AUTH_ROUTE
        }

        setContent {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val notificationPermission = rememberPermissionState(
                    permission = Manifest.permission.POST_NOTIFICATIONS
                )
                LaunchedEffect(key1 = Unit) {
                    if (!notificationPermission.status.isGranted) {
                        notificationPermission.launchPermissionRequest()
                    }
                }
            }

            NimbleSurveyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NimbleSurveyNavHost(
                        startDestination = startDestination,
                        authState = authState,
                        logout = viewModel::logout
                    )
                }
            }
        }
    }
}