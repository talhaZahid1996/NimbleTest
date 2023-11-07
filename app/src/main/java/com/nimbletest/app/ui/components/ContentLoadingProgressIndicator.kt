package com.nimbletest.app.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime

/**
 * Inspired by View-based [ContentLoadingProgressBar](https://developer.android.com/reference/androidx/core/widget/ContentLoadingProgressBar).
 *
 * Waits `minDelayDuration` until contents of `loading` composable is shown when `isLoading` is
 * true. Once loading is shown and `isLoading` becomes `false`, waits `minShowDuration`
 * to avoid flashes in the UI. If `isLoading` becomes `false` before
 * `minDelayDuration` passes, the loading state is not shown at all.
 *
 * @param isLoading Whether loading or content state should be shown
 * @param minDelayDuration Minimum delay until loading state is shown
 * @param minShowDuration Minimum delay until loading state is dismissed if active
 *
 * */
@Composable
@ExperimentalTime
fun ContentLoadingProgressIndicator(
    isLoading: Boolean,
    minDelayDuration: Duration = 500.milliseconds,
    minShowDuration: Duration = 500.milliseconds,
) {
    var showLoading by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(isLoading) {
        if (isLoading && !showLoading) {
            delay(minDelayDuration)
            showLoading = true
        } else if (!isLoading && showLoading) {
            delay(minShowDuration)
            showLoading = false
        }
    }

    if (showLoading) {
        // made like this to show loading properly
        LoadingDialog()
    }
}

@Composable
fun LoadingDialog() {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = 0.dp,
            backgroundColor = MaterialTheme.colorScheme.primary,
        ) {
            CircularProgressIndicator(
                strokeCap = StrokeCap.Round,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}