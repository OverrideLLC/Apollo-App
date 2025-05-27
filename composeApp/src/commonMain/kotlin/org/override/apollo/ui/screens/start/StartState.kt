package org.override.apollo.ui.screens.start

import androidx.compose.runtime.*

@Immutable
data class StartState(
    val isLoading: Boolean = false,
    val emailText: String = "",
    val passwordText: String = "",
    val loginSuccess: Boolean = false,
    val qrCode: String? = null,
    val qrExpiration: Long? = null,
    val errorMessage: String? = null
)