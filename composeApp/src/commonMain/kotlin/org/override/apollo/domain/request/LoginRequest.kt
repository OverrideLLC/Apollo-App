package org.override.apollo.domain.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String
)