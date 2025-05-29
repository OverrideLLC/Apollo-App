package org.override.apollo.domain.request

import kotlinx.serialization.Serializable
import org.override.apollo.utils.data.Student

@Serializable
data class StudentRequest(
    val student: Student? = null,
    val courseId: String? = null,
)