package org.override.apollo.domain

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

// Asume que tienes una forma de saber si estás en modo DEBUG
object BuildConfig {
    const val DEBUG = true // Cambia esto según tu entorno de compilación
}

actual val commonHttpClient: HttpClient by lazy { // Usando lazy para inicialización única
    HttpClient(CIO) { // O el motor que prefieras/necesites

        // 1. Negociación de Contenido (JSON)
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = BuildConfig.DEBUG // Solo para debug
                    isLenient = true
                    ignoreUnknownKeys = true
                    encodeDefaults = true // Considera si lo necesitas
                }
            )
        }

        // 2. Logging
        install(Logging)

        // 3. Timeouts
        install(HttpTimeout) {
            requestTimeoutMillis = 30000 // 30 segundos
            connectTimeoutMillis = 15000 // 15 segundos
            socketTimeoutMillis = 15000  // 15 segundos
        }

        // 4. Reintentos (opcional pero recomendado)
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 3)
            exponentialDelay() // Retardo exponencial entre reintentos
            // Podrías querer ser más específico, ej: solo reintentar en timeouts o ciertos códigos
            // retryOnException(maxRetries = 2, retryOnTimeout = true)
            // retryIf { request, response -> response.status == HttpStatusCode.ServiceUnavailable }
        }

        // 5. Configuración de Solicitudes por Defecto (opcional)
        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            // Podrías añadir un User-Agent aquí
            // header(HttpHeaders.UserAgent, "MiAppCliente/1.0")

            // Si todas tus llamadas son a la misma API base (ejemplo)
            // url {
            //     protocol = URLProtocol.HTTPS
            //     host = "api.tu-servicio.com"
            //     path("/v1/")
            // }
        }

        // 6. Autenticación (opcional, si es necesaria globalmente)
        // install(Auth) {
        //     bearer {
        //         loadTokens {
        //             // Lógica para cargar tus tokens
        //             fetchTokensFromSecureStorage()
        //         }
        //         refreshTokens {
        //             // Lógica para refrescar tokens
        //             try {
        //                 val tokenResponse: YourTokenResponseClass = client.post {
        //                     markAsRefreshTokenRequest() // Para evitar bucles de reintento de Auth
        //                     url("https://tu-api.com/auth/refresh")
        //                     // ... enviar refreshToken antiguo
        //                 }.body()
        //                 BearerTokens(tokenResponse.accessToken, tokenResponse.refreshToken)
        //             } catch (e: Exception) {
        //                 // Manejar fallo de refresh, quizás limpiar tokens y requerir nuevo login
        //                 null
        //             }
        //         }
        //     }
        // }

        // 7. Caché HTTP (opcional)
        // install(HttpCache) {
        //     // privateStorage(MemoryStorage(maxSize = 5 * 1024 * 1024)) // Caché en memoria de 5MB
        // }

        // Configuración específica del motor si es necesario
        engine {
            // Ejemplo para CIO:
            // requestTimeout = 0 // Deshabilitar timeout del motor si se usa HttpTimeout plugin
            // endpoint {
            //     maxConnectionsPerRoute = 100
            //     pipelineMaxSize = 20
            //     keepAliveTime = 5000
            //     connectTimeout = 10000
            //     connectAttempts = 5
            // }
        }

        // Ktor 2.0+ tiene expectSuccess = true por defecto para el cliente.
        // Si usas una versión anterior o quieres manejar errores manualmente:
        // expectSuccess = false
    }
}