package org.override.apollo.ui.screens.home.screens.ai.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import org.koin.compose.viewmodel.koinViewModel
import org.override.apollo.ui.screens.home.screens.ai.AiAction
import org.override.apollo.ui.screens.home.screens.ai.AiState
import org.override.apollo.ui.screens.home.screens.ai.AiViewModel

@Composable
internal fun ContentAi(
    viewModel: AiViewModel = koinViewModel(),
    state: AiState,
    displayedText: String,
    onAction: (AiAction) -> Unit,
    alpha: Animatable<Float, *>,
) {
    val messages = state.messages
    val newMessage = remember { mutableStateOf("") }
    var value by remember { mutableStateOf(TextFieldValue(newMessage.value)) }
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        if (state.messages.isNotEmpty()) {
            Chat(
                messages = messages,
                modifier = Modifier.weight(1f),
                isLoading = state.isLoading,
                onAction = viewModel::onAction
            )
        } else {
            Eva(
                displayedText = displayedText,
                alpha = alpha
            )
        }
        TextFieldEva(
            viewModel = viewModel,
            state = state,
            value = value,
            onAction = onAction,
            onNewMessage = { newMessage.value = it },
            onChangeValue = { value = it }
        )
    }
}