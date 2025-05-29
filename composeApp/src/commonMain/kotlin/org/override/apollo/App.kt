package org.override.apollo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.override.apollo.ui.navigation.NavigationStart
import org.override.apollo.ui.theme.ApolloTheme

@Composable
fun App() {
    ApolloTheme(
        darkTheme = true
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            contentColor = colorScheme.background,
            content = { NavigationStart() }
        )
    }
}