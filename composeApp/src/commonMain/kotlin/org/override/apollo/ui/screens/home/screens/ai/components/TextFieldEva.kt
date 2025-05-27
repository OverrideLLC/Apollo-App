package org.override.apollo.ui.screens.home.screens.ai.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.override.apollo.ui.screens.home.screens.ai.AiAction
import org.override.apollo.ui.screens.home.screens.ai.AiState
import org.override.apollo.ui.screens.home.screens.ai.AiViewModel
import org.override.apollo.ui.screens.home.screens.ai.utils.detectServices

@Composable
internal fun TextFieldEva(
    viewModel: AiViewModel = koinViewModel(),
    state: AiState,
    value: TextFieldValue,
    onAction: (AiAction) -> Unit,
    onNewMessage: (String) -> Unit,
    onChangeValue: (TextFieldValue) -> Unit
) {
    TextFieldAi(
        value = value,
        onValueChange = { onChangeValue(it) },
        state = state,
        modifier = Modifier.padding(
            bottom = 8.dp,
            start = 16.dp,
            end = 16.dp
        ),
        onAttachFile = {  },
        onSend = { detectedService, message ->
            detectServices(
                message = message,
                state = state,
                value = value,
                onAction = viewModel::onAction,
                detectedService = detectedService ?: "",
                onNewMessage = { onNewMessage(it) },
                valueChange = { onChangeValue(it) }
            )
        }
    )
}