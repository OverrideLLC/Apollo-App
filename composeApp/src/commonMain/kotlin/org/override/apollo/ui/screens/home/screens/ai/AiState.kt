package org.override.apollo.ui.screens.home.screens.ai

import androidx.compose.runtime.*
import dev.shreyaspatil.ai.client.generativeai.Chat
import org.override.apollo.ui.screens.home.screens.ai.AiViewModel.Message
import org.override.apollo.utils.data.WorkData

@Immutable
data class AiState(
    val isLoading: Boolean = false,
    val isLoadingData: Boolean = true,
    val isRunCode: Boolean = false,
    val messages: List<Message> = emptyList(),
    val errorMessage: String? = null,
    val chat: Chat? = null,
    val selectedService: String? = null,
    val announcement: String? = null,
    val announcements: Boolean = false,
    val showReport: Boolean = false,
    val work: WorkData? = null,
    val workText: String? = null
)