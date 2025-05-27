package org.override.apollo.domain

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json

actual val commonHttpClient: HttpClient
    get() = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
        install(Logging) { }
    }
