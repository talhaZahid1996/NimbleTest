package com.nimbletest.app.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import com.nimbletest.app.R

@Composable
@Preview
fun CustomAlertDialog(
    modifier: Modifier = Modifier,
    titleString: String = "",
    textString: String = "",
    confirmButtonText: Int = R.string.ok_label,
    dismissButtonText: Int = R.string.btn_cancel_label,
    hasRectangleShape: Boolean = false,
    showDismissButton: Boolean = true,
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    customDialogContent: (@Composable () -> Unit)? = null,
    onConfirmationPressed: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    AlertDialog(
        modifier = modifier,
        shape = if (hasRectangleShape) AlertDialogDefaults.shape else MaterialTheme.shapes.large,
        title = {
            Text(titleString)
        },
        text = {
            if (customDialogContent != null) {
                customDialogContent()
            } else {
                Text(textString)
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirmationPressed) {
                Text(stringResource(id = confirmButtonText))
            }
        },
        dismissButton = {
            if (showDismissButton) {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(id = dismissButtonText))
                }
            }
        },
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside
        )
    )
}