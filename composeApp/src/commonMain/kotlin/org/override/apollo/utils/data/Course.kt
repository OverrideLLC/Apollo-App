package org.override.apollo.utils.data

import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val id: String? = null, // Hecho nulable, aunque los IDs suelen ser no nulos
    val name: String? = null,
    val career: String? = null,
    val section: String? = null,
    val degree: String? = null,
    val teacherId: String? = null,
    val studentIds: List<String>? = null, // Lista de IDs de estudiantes
    val attendanceIds: Map<String, List<String>>? = null, // Clave: fecha, Valor: Lista de IDs de asistencia
    val assignmentsIds: List<String>? = null //Lista de IDs de tareas
)
