package org.override.apollo.ui.screens.home.screens.ai

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.shreyaspatil.ai.client.generativeai.common.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.override.apollo.domain.repositories.GeminiRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AiViewModel(
    private val geminiRepository: GeminiRepository
) : ViewModel() {

    @Immutable
    data class Message @OptIn(ExperimentalUuidApi::class) constructor(
        val id: String = Uuid.random().toString(),
        val text: String,
        val isUser: Boolean,
    )

    private val _state = MutableStateFlow(AiState())
    val state = _state
        .onStart { loadData() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AiState()
        )

    fun onAction(action: AiAction) {
        when (action) {
            is AiAction.ClearChat -> clearChat()
            is AiAction.RunCode -> runCode(action.code)
            is AiAction.AnnounceClassroom -> announceClassroom(action.message)
            is AiAction.GetAnnouncementsClassroom -> getAnnouncementsClassroom(action.message)
            is AiAction.UpdateAssignmentClassroom -> updateAssignmentClassroom(action.message)
            is AiAction.WorkerRanking -> workerRanking(action.message)
            is AiAction.ShowReportClassroom -> showReportClassroom(action.message)
            is AiAction.SendMessage -> sendMessage(action.message)
        }
    }

    private fun sendMessage(messageText: String) {
        if ((messageText.isBlank()) || state.value.isLoading || state.value.chat == null) return

        Log.w(
            "AiViewModel",
            "Sending message: $messageText"
        )
        _state.update { currentState ->
            currentState.copy(
                messages = currentState.messages + Message(text = messageText, isUser = true),
                errorMessage = null,
            )
        }

        viewModelScope.launch {
            Log.w(
                "AiViewModel",
                "Sending message: $messageText"
            )
            try {
                _state.update { it.copy(isLoading = true) }

                val response = geminiRepository.sendMessage(
                    chat = state.value.chat!!,
                    message = messageText
                )

                Log.w(
                    "AiViewModel",
                    "Response: $response"
                )

                // Añadir respuesta del modelo a la UI
                _state.update { currentState ->
                    currentState.copy(
                        messages = currentState.messages + Message(text = response, isUser = false),
                        isLoading = false // Finalizar carga
                    )
                }
                Log.w(
                    "AiViewModel",
                    "Message sent successfully"
                )
            } catch (e: Exception) {
                // Manejar error
                _state.update { currentState ->
                    // Restaurar los archivos seleccionados si hubo un error para que el usuario pueda reintentar
                    currentState.copy(
                        isLoading = false,
                        errorMessage = "Error al obtener respuesta: ${e.message ?: "Error desconocido"}",
                    )
                }
                Log.w(
                    "AiViewModel",
                    "Error al enviar mensaje: ${e.message ?: "Error desconocido"}"
                )
                e.printStackTrace()
                // Opcionalmente, añadir un mensaje de error a la lista de mensajes
                _state.update { currentState ->
                    currentState.copy(
                        messages = currentState.messages + Message(
                            text = "Error: ${e.message}",
                            isUser = false
                        )
                    )
                }
            }
        }
    }

    private fun showReportClassroom(string: String) {

    }

    private fun workerRanking(string: String) {

    }

    private fun updateAssignmentClassroom(string: String) {

    }

    private fun getAnnouncementsClassroom(string: String) {

    }

    private fun announceClassroom(string: String) {

    }

    private fun loadData() {
        _state.update { it.copy(isLoadingData = true, errorMessage = null) }
        viewModelScope.launch {
            try {
                val chat = geminiRepository.startChat()
                _state.update {
                    it.copy(chat = chat, isLoadingData = false)
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoadingData = false,
                        errorMessage = "Error al iniciar el chat: ${e.message ?: "Error desconocido"}"
                    )
                }
                e.printStackTrace()
            }
        }
    }

    private fun clearChat() {
        _state.value = _state.value.copy(
            messages = emptyList()
        )
    }

    private fun runCode(code: String) {
        _state.value = _state.value.copy(
            isRunCode = true
        )
    }

}