package com.nimbletest.app.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// unused
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val NimbleWhite = Color(0xFFFFFFFF)
val NimbleBlack = Color(0xFF000000)

val NimbleDarkGrey = Color(0xFF44464F)
val NimbleLightGrey = Color(0xFFAEAEB1)
val NimbleDarkerGrey = Color.DarkGray

@Immutable
data class NimbleCustomColors(
    val nimbleDarkGrey: Color = NimbleDarkGrey,
    val nimbleLightGrey: Color = NimbleLightGrey,
    val nimbleDarkerGrey: Color = NimbleDarkerGrey,
)

val LocalNimbleColors = staticCompositionLocalOf { NimbleCustomColors() }