package org.override.apollo.ui.screens.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.override.apollo.domain.repositories.LoginRepository
import kotlin.time.Duration.Companion.minutes

class StartViewModel(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _state = MutableStateFlow(StartState())
    val state = _state
        .onStart { generateQrCode() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = StartState()
        )

    fun onAction(action: StartAction) {
        when (action) {
            is StartAction.EmailChanged -> {
                _state.value = _state.value.copy(
                    emailText = action.email,
                    errorMessage = null
                )
            }

            is StartAction.PasswordChanged -> {
                _state.value = _state.value.copy(
                    passwordText = action.password,
                    errorMessage = null
                )
            }

            is StartAction.Login -> {
                login()
            }

            is StartAction.RefreshQr -> {
                generateQrCode()
            }

            is StartAction.QrLoginSuccess -> {
                _state.value = _state.value.copy(
                    loginSuccess = true,
                    isLoading = false
                )
            }
        }
    }

    private fun login() {
        _state.value = _state.value.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            try {
                println("Attempting login with email: ${_state.value.emailText}")
                val response = loginRepository.login(_state.value.emailText)
                println("Login call successful. Raw Response: $response")

                _state.value = _state.value.copy(
                    loginSuccess = response.success,
                    isLoading = false,
                    errorMessage = if (!response.success) "Error al iniciar sesión" else null
                )
                println("State updated. loginSuccess: ${response.success}")

            } catch (e: Exception) {
                println("Error during login: ${e::class.simpleName} - ${e.message}")
                e.printStackTrace()

                _state.value = _state.value.copy(
                    isLoading = false,
                    loginSuccess = false,
                    errorMessage = "Error al iniciar sesión: ${e.message}"
                )
            }
        }
    }

    private fun generateQrCode() {
        _state.value = _state.value.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            try {
                kotlinx.coroutines.delay(1000)

                val now = Clock.System.now()
                val qrCode = "qr_code_data_${now.toEpochMilliseconds()}"
                val expiration = now + 5.minutes

                _state.value = _state.value.copy(
                    isLoading = false,
                    qrCode = qrCode,
                    qrExpiration = expiration.toEpochMilliseconds()
                )

                // Aquí podrías iniciar un polling para verificar si el QR fue escaneado
                startQrPolling()

            } catch (e: Exception) {
                println("Error generating QR: ${e::class.simpleName} - ${e.message}")
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Error al generar código QR: ${e.message}"
                )
            }
        }
    }

    private fun startQrPolling() {
        viewModelScope.launch {
            repeat(30) {
                kotlinx.coroutines.delay(10_000)

                val isScanned = checkQrStatus(_state.value.qrCode)
                if (isScanned) {
                    onAction(StartAction.QrLoginSuccess)
                    return@launch
                }

                // Verificar si el QR expiró
                val currentTime = Clock.System.now()
                if (_state.value.qrExpiration != null && currentTime.toEpochMilliseconds() > _state.value.qrExpiration!!) {
                    _state.value = _state.value.copy(
                        qrCode = null,
                        qrExpiration = null,
                        errorMessage = "El código QR ha expirado"
                    )
                    return@launch
                }
            }
        }
    }

    private suspend fun checkQrStatus(qrCode: String?): Boolean {

        return false
    }
}