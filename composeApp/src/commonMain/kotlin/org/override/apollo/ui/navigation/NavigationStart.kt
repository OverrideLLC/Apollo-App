package org.override.apollo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.override.apollo.ui.screens.home.HomeRoot
import org.override.apollo.ui.screens.start.StartRoot
import org.override.apollo.utils.route.RoutesStart

@Composable
fun NavigationStart() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = RoutesStart.Home.route
    ) {
        composable(RoutesStart.Start.route) {
            StartRoot(navController = navController)
        }
        composable(RoutesStart.Home.route) {
            HomeRoot()
        }
    }
}