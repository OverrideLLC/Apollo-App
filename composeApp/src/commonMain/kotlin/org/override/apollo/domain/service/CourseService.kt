package org.override.apollo.domain.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.override.apollo.domain.response.CourseResponse
import org.override.apollo.utils.constants.Constants.URL_BACKEND
import org.override.apollo.utils.data.Course

class CourseService(private val client: HttpClient) {
    suspend fun getCourses(): CourseResponse {
        return try {
            CourseResponse(
                courses = client.get("$URL_BACKEND/all").body(),
                isSuccess = true
            )
        } catch (e: Exception) {
            CourseResponse(
                errorMessage = e.message,
                isSuccess = false
            )
        }
    }

    suspend fun getCourseById(id: String): CourseResponse {
        return try {
            CourseResponse(
                courses = client.get("$URL_BACKEND/course/$id").body(),
                isSuccess = true
            )
        } catch (e: Exception) {
            CourseResponse(
                errorMessage = e.message,
                isSuccess = false
            )
        }
    }

    suspend fun createCourse(course: Course) {
        try {
            client.post {
                url("${URL_BACKEND}course/create")
                contentType(ContentType.Application.Json)
                setBody(course)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}