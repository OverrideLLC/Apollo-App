package org.override.apollo.domain.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val success: Boolean,
    val errorMessage: String? = null,
    val errorCode: String? = null,
    val userId: String? = null,
    val token: String? = null,
    val displayName: String? = null,
    val email: String? = null,
    val photoUrl: String? = null
)