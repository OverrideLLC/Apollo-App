package org.override.apollo.di

import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.override.apollo.domain.commonHttpClient
import org.override.apollo.domain.repositories.CourseRepository
import org.override.apollo.domain.repositories.GeminiRepository
import org.override.apollo.domain.repositories.LoginRepository
import org.override.apollo.domain.repositories.StudentRepository
import org.override.apollo.domain.service.CourseService
import org.override.apollo.domain.service.GeminiService
import org.override.apollo.domain.service.LoginService
import org.override.apollo.domain.service.StudentService

val DomainModule: Module
    get() = module {
        single<HttpClient> { commonHttpClient }
        factoryOf(::LoginRepository)
        factoryOf(::GeminiRepository)
        factoryOf(::CourseRepository)
        factoryOf(::StudentRepository)

        factoryOf(::LoginService)
        factoryOf(::GeminiService)
        factoryOf(::CourseService)
        factoryOf(::StudentService)
    }