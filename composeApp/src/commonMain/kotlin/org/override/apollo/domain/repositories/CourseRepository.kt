package org.override.apollo.domain.repositories

import io.ktor.client.HttpClient
import org.override.apollo.utils.data.Course

class CourseRepository() {
    suspend fun getCourses(): List<Course> {
        return
    }
}