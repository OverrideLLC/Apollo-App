package org.override.apollo.domain.response

import kotlinx.serialization.Serializable
import org.override.apollo.utils.data.Course

@Serializable
data class CourseResponse(
    val courses: List<Course>? = null,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val errorCode: String? = null,
)