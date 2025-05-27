package org.override.apollo.utils.route

sealed class RoutesStart(val route: String) {
    object Start : RoutesStart("start")
    object Home : RoutesStart("home")
}