package com.nimbletest.app.ui.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.nimbletest.app.ui.theme.LocalNimbleColors

@Composable
fun TopLoaderShimmer() {
    val contentHeight = 20.dp

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(contentHeight)
                    .fillMaxWidth(.4f)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .height(contentHeight)
                    .fillMaxWidth(.3f)
                    .shimmerEffect()
            )
        }
        // circle
        Box(
            modifier = Modifier
                .height(40.dp)
                .width(40.dp)
                .shimmerEffect()
        )
    }
}

@Composable
fun BottomLoaderShimmer() {
    val contentHeight = 20.dp

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .height(contentHeight)
                .fillMaxWidth(.1f)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .height(contentHeight)
                .fillMaxWidth(.7f)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .height(contentHeight)
                .fillMaxWidth(.4f)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .height(contentHeight)
                .fillMaxWidth(.9f)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .height(contentHeight)
                .fillMaxWidth(.5f)
                .shimmerEffect()
        )
    }
}

fun Modifier.shimmerEffect(
    colors: List<Color> = emptyList(),
    animationDuration: Int = 1500
): Modifier = composed {
    val nimbleColors = LocalNimbleColors.current

    val shimmerColors by remember {
        mutableStateOf(
            colors.ifEmpty {
                listOf(
                    nimbleColors.nimbleDarkGrey.copy(alpha = 0.3f),
                    nimbleColors.nimbleLightGrey.copy(alpha = 0.3f),
                    nimbleColors.nimbleDarkGrey.copy(alpha = 0.3f),
                )
            }
        )
    }

    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition(label = "")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration)
        ),
        label = ""
    )

    background(
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        ),
        shape = CircleShape
    ).onGloballyPositioned {
        size = it.size
    }
}