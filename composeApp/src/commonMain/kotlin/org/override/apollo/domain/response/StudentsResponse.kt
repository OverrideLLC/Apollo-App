package org.override.apollo.domain.response

import kotlinx.serialization.Serializable
import org.override.apollo.utils.data.Student

@Serializable
data class StudentResponse(
    val students: List<Student>? = null,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
)