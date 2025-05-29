package org.override.apollo.utils.data

import kotlinx.serialization.Serializable

@Serializable
data class Student(
    val id: String,
    val courseIds: List<String>, // Lista de IDs de cursos
    val email: String,
    val displayName: String,
    val phone: String? = null,
    val tuition: String? = null,
)
