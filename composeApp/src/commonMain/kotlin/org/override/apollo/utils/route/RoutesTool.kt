package org.override.apollo.utils.route

sealed class RoutesTool(val route: String) {
    object TakeAttendees : RoutesTool("take_attendees")
    object AddClass : RoutesTool("add_class")
    object AddStudent : RoutesTool("add_student")
    object Announce : RoutesTool("announce")
    object Ratings : RoutesTool("ratings")
    object Courses : RoutesTool("courses")
}