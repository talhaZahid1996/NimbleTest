package com.nimbletest.app.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun NimbleButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    isEnabled: Boolean = true,
    onPressed: () -> Unit = {},
) {

    Button(
        modifier = modifier,
        onClick = onPressed,
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
        ),
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = buttonText,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}