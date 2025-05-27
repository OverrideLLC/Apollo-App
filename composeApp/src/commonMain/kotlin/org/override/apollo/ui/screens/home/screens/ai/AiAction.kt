package org.override.apollo.ui.screens.home.screens.ai

sealed interface AiAction {
    object ClearChat : AiAction
    data class RunCode(val code: String): AiAction
    data class AnnounceClassroom(val message: String): AiAction
    data class GetAnnouncementsClassroom(val message: String): AiAction
    data class UpdateAssignmentClassroom(val message: String): AiAction
    data class WorkerRanking(val message: String): AiAction
    data class ShowReportClassroom(val message: String): AiAction
    data class SendMessage(val message: String): AiAction
}