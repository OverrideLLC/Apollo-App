package org.override.apollo.utils.enums

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import apollo_app.composeapp.generated.resources.Res
import apollo_app.composeapp.generated.resources.add_circle_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import apollo_app.composeapp.generated.resources.fact_check_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import apollo_app.composeapp.generated.resources.person_add_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import org.jetbrains.compose.resources.DrawableResource
import org.override.apollo.utils.route.RoutesTool

enum class Tools(
    val icon: DrawableResource,
    val nameString: String,
    val route: String,
    val size: DpSize
) {
    TAKE_ATTENDEES(
        icon = Res.drawable.fact_check_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24,
        route = RoutesTool.TakeAttendees.route,
        nameString = "Tomar Asistencia", // Traducido de "Take Attendees"
        size = DpSize(width = 1200.dp, height = 1200.dp)
    ),
    ADD_CLASS(
        icon = Res.drawable.add_circle_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24,
        route = RoutesTool.AddClass.route,
        nameString = "Agregar Clase", // Traducido de "Add Class"
        size = DpSize(width = 1200.dp, height = 720.dp)
    ),
    ADD_STUDENT(
        icon = Res.drawable.person_add_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24,
        route = RoutesTool.AddStudent.route,
        nameString = "Agregar Estudiante", // Traducido de "Add Student"
        size = DpSize(width = 600.dp, height = 850.dp)

    ),
}