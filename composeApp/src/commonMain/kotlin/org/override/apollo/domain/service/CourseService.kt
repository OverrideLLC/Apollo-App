package org.override.apollo.domain.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.override.apollo.utils.constants.Constants.URL_BACKEND
import org.override.apollo.utils.data.Course

class CourseService(private val client: HttpClient) {
    suspend fun getCourses(): List<Course> {
        return client.get("$URL_BACKEND/courses").body()
    }
}