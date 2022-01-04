package com.ryancasler.solscan.core.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Animated(
    visible: Boolean,
    modifier: Modifier = Modifier,
    createContent: @Composable () -> Unit = {}
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = fadeIn(initialAlpha = 0.4f),
        exit = fadeOut(animationSpec = tween(durationMillis = 250)),
        content = { createContent() }
    )
}