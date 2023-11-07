package com.nimbletest.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NimbleTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    placeHolderText: String = "",
    errorMessage: String = "",
    onValueChange: (String) -> Unit = {},
    imeAction: ImeAction = ImeAction.Default,
    onlyShowCloseTrailIcon: Boolean = false,
    isPasswordTextField: Boolean = false,
    onCloseClicked: () -> Unit = {},
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    var searchValue by rememberSaveable { mutableStateOf(value) }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    BasicTextField(
        value = searchValue,
        onValueChange = {
            searchValue = it
            onValueChange(it)
        },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        keyboardActions = KeyboardActions(
            onSend = { focusManager.clearFocus() },
            onDone = { focusManager.clearFocus() }
        ),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = Color.White
        ),
        cursorBrush = Brush.verticalGradient(
            listOf(Color.White, Color.White)
        ),
        visualTransformation = if (isPasswordTextField && !showPassword) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.DarkGray.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(16.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    Modifier
                        .weight(8f)
                        .padding(vertical = 24.dp, horizontal = 16.dp)
                ) {
                    if (searchValue.isEmpty()) {
                        Text(
                            text = placeHolderText,
                            maxLines = 1,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = if (searchValue.isEmpty()) Color.White.copy(alpha = 0.3f) else Color.White
                            ),
                        )
                    }
                    innerTextField()
                }
                Box(Modifier.weight(2f)) {
                    when {
                        searchValue.isEmpty() && !onlyShowCloseTrailIcon -> {
                            leadingIcon?.invoke()
                        }

                        searchValue.isNotEmpty() && onlyShowCloseTrailIcon -> {
                            IconButton(
                                onClick = {
                                    searchValue = ""
                                    onValueChange("")
                                    focusManager.clearFocus()
                                    onCloseClicked()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    tint = Color.White,
                                    contentDescription = null
                                )
                            }
                        }

                        searchValue.isNotEmpty() && isPasswordTextField -> {
                            IconButton(
                                modifier = Modifier.align(Alignment.CenterEnd),
                                onClick = {
                                    showPassword = !showPassword
                                }
                            ) {
                                Icon(
                                    imageVector = if (showPassword) {
                                        Icons.Default.VisibilityOff
                                    } else {
                                        Icons.Default.Visibility
                                    },
                                    tint = Color.White,
                                    contentDescription = null
                                )
                            }
                        }

                        else -> Unit
                    }
                }
            }
        },
    )
    AnimatedVisibility(
        visible = errorMessage.isNotEmpty(),
        enter = slideInVertically() + expandVertically() + fadeIn(),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        ErrorLabel(errorMessage)
    }
}

@Composable
private fun ErrorLabel(errorMessage: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .padding(start = 16.dp)
                .size(12.dp),
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            fontSize = 12.sp,
            maxLines = 2
        )
    }
}