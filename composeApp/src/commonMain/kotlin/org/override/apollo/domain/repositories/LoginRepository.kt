package org.override.apollo.domain.repositories

import org.override.apollo.domain.response.LoginResponse
import org.override.apollo.domain.service.LoginService

class LoginRepository(private val loginService: LoginService) {
    suspend fun login(email: String): LoginResponse {
        return loginService.login(email)
    }
}