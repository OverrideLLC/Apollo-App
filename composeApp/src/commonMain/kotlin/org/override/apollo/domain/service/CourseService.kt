package org.override.apollo.domain.service

import dev.shreyaspatil.ai.client.generativeai.common.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.override.apollo.domain.request.StudentRequest
import org.override.apollo.domain.response.CourseResponse
import org.override.apollo.utils.constants.Constants.URL_BACKEND
import org.override.apollo.utils.data.Course

class CourseService(private val client: HttpClient) {
    suspend fun getCourses(): CourseResponse {
        return try {
            val response = client.get("${URL_BACKEND}course/all")
            val courses = response.body<CourseResponse>()
            Log.w("CourseService", "Loaded courses: ${courses.courses}")
            courses
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
                courses = client.get("${URL_BACKEND}course/$id").body(),
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

    suspend fun updateCourse(course: Course) {
        try {
            client.post {
                url("${URL_BACKEND}course/update")
                contentType(ContentType.Application.Json)
                setBody(course)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteCourse(id: String) {
        try {
            client.delete("${URL_BACKEND}course/$id").body<Unit>()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}