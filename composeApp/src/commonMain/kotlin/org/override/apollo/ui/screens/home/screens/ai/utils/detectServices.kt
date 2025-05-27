package org.override.apollo.ui.screens.home.screens.ai.utils

import androidx.compose.ui.text.input.TextFieldValue
import dev.shreyaspatil.ai.client.generativeai.common.util.Log
import org.override.apollo.ui.screens.home.screens.ai.AiAction
import org.override.apollo.ui.screens.home.screens.ai.AiState
import org.override.apollo.ui.screens.home.screens.ai.AiViewModel

internal fun detectServices(
    message: String,
    state: AiState,
    value: TextFieldValue,
    detectedService: String,
    onAction: (AiAction) -> Unit,
    onNewMessage: (String) -> Unit,
    valueChange: (TextFieldValue) -> Unit
) {
    when (detectedService) {
        "Classroom ejecutar" -> {
            onAction(AiAction.RunCode(message))
            valueChange(TextFieldValue(""))
            onNewMessage("")
        }

        "Classroom anunciar" -> {
            onAction(AiAction.AnnounceClassroom(message))
            valueChange(TextFieldValue(""))
            onNewMessage("")
        }

        "Classroom anuncios" -> {
            onAction(AiAction.GetAnnouncementsClassroom(message))
            valueChange(TextFieldValue(""))
            onNewMessage("")
        }

        "Classroom reporte" -> {
            onAction(AiAction.ShowReportClassroom(message))
            valueChange(TextFieldValue(""))
            onNewMessage("")
        }

        "Classroom subir tarea" -> {
            onAction(AiAction.UpdateAssignmentClassroom(message))
            valueChange(TextFieldValue(""))
            onNewMessage("")
        }

        "Classroom analizar trabajo" -> {
            onAction(AiAction.WorkerRanking(message))
            valueChange(TextFieldValue(""))
            onNewMessage("")
        }

        else -> {
            if (value.text.isNotBlank() && !state.isLoading) {
                print(message)
                onAction(AiAction.SendMessage(message))
                valueChange(TextFieldValue(""))
                onNewMessage("")
            }
        }
    }
}