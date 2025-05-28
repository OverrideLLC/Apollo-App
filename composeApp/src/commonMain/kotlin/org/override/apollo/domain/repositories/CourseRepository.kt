package org.override.apollo.domain.repositories

import org.override.apollo.domain.service.CourseService
import org.override.apollo.utils.data.Course

class CourseRepository(
    private val service: CourseService,
) {
    suspend fun getCourses(): List<Course> {
        return service.getCourses().courses ?: emptyList()
    }

    suspend fun createCourse(course: Course) {
        service.createCourse(course)
    }

    suspend fun getCourseById(id: String): Course? {
        return service.getCourseById(id).courses?.firstOrNull()
    }
}