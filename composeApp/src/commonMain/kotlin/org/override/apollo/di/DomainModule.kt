package org.override.apollo.di

import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.override.apollo.domain.commonHttpClient
import org.override.apollo.domain.repositories.GeminiRepository
import org.override.apollo.domain.repositories.LoginRepository
import org.override.apollo.domain.service.GeminiService
import org.override.apollo.domain.service.LoginService

val DomainModule: Module
    get() = module {
        single<HttpClient> { commonHttpClient }
        factoryOf(::LoginRepository)
        factoryOf(::GeminiRepository)

        factoryOf(::LoginService)
        factoryOf(::GeminiService)
    }