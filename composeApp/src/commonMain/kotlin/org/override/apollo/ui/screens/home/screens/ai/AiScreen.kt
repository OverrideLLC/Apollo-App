package org.override.apollo.ui.screens.home.screens.ai

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.override.apollo.ui.screens.home.screens.ai.components.AnimationEva
import org.override.apollo.ui.screens.home.screens.ai.components.ContentAi

@Composable
fun AiRoot(
    modifier: Modifier = Modifier,
    viewModel: AiViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AiScreen(
        state = state,
        modifier = modifier,
        onAction = viewModel::onAction
    )
}

@Composable
private fun AiScreen(
    state: AiState,
    modifier: Modifier = Modifier,
    onAction: (AiAction) -> Unit,
) {
    Box(
        modifier = modifier
            .background(colorScheme.surfaceContainer.copy(alpha = 0.7f))
            .clip(MaterialTheme.shapes.medium),
        content = {
            val initiallyVisible = state.messages.isNotEmpty()
            val text = "Hola, ¿en qué puedo ayudarte?"
            var displayedText by remember(
                text,
                initiallyVisible
            ) { mutableStateOf(if (initiallyVisible) text else "") }
            val alpha =
                remember(text, initiallyVisible) { Animatable(if (initiallyVisible) 1f else 0f) }

            AnimationEva(
                text = text,
                initiallyVisible = initiallyVisible,
                alpha = alpha,
                onDisplayedTextChange = { displayedText = it }
            )

            ContentAi(
                state = state,
                displayedText = displayedText,
                alpha = alpha,
                onAction = onAction
            )
        }
    )
}