package org.override.apollo.ui.screens.start

sealed interface StartAction {
    data class EmailChanged(val email: String) : StartAction
    data class PasswordChanged(val password: String) : StartAction
    object Login : StartAction
    object RefreshQr : StartAction
    object QrLoginSuccess : StartAction
}