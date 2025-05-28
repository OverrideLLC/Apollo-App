package org.override.apollo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.override.apollo.ui.screens.home.screens.tools.screens.add_course.AddCourseRoot
import org.override.apollo.ui.screens.home.screens.tools.screens.add_student.AddStudentRoot
import org.override.apollo.utils.route.RoutesTool

@Composable
fun NavigationTools(
    route: String
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = route
    ) {
        composable(RoutesTool.AddClass.route) {
            AddCourseRoot()
        }
        composable(RoutesTool.AddStudent.route) {
            AddStudentRoot()
        }
        composable(RoutesTool.TakeAttendees.route) {
        }
        composable(RoutesTool.Ratings.route) {
        }
        composable(RoutesTool.Announce.route) {
        }
    }
}