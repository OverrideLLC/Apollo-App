package org.override.apollo.domain.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.override.apollo.domain.request.LoginRequest
import org.override.apollo.domain.response.LoginResponse
import org.override.apollo.utils.constants.Constants.URL_BACKEND

class LoginService(private val httpClient: HttpClient) {
    suspend fun login(email: String): LoginResponse {
        return try {
            val response = httpClient.post {
                url("${URL_BACKEND}auth/signin/email")
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email))
            }.body<LoginResponse>()
            response
        } catch (e: Exception) {
            println("Error durante el login: ${e.message}")
            LoginResponse(false)
        }
    }
}
