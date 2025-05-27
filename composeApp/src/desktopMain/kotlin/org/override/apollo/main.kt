package org.override.apollo

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.override.apollo.di.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "Apollo-App",
        state = rememberWindowState(placement = WindowPlacement.Maximized)
    ) {
        App()
    }
}