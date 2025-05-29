import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    "kotlin"
    kotlin("plugin.serialization") version "2.1.21"
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvm("desktop")
    jvmToolchain(17)
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs()

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            //Compose
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.androidx.navigation)

            //Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.contentNegotiation) // Para JSON, etc.
            implementation(libs.kotlinx.serialization.json)    // La biblioteca kotlinx.serialization en sí
            implementation(libs.ktor.client.logging)

            //Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.composeVM)

            //Utils
            implementation("org.jetbrains:markdown:0.7.3")

            //KotlinX
            implementation(libs.kotlinx.datetime)
            implementation(libs.ktor.serialization.kotlinx.json)

            //Ai
            implementation("dev.shreyaspatil.generativeai:common:0.9.0-1.1.0")
            implementation("dev.shreyaspatil.generativeai:generativeai-google:0.9.0-1.1.0")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.ktor.client.cio.jvm)
            implementation(libs.slf4j)
        }
        wasmJsMain.dependencies {
            implementation(libs.ktor.client.js)
        }
    }
}

dependencies {

}