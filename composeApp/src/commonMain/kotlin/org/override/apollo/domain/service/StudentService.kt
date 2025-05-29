package org.override.apollo.domain.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.override.apollo.domain.request.StudentRequest
import org.override.apollo.domain.response.StudentResponse
import org.override.apollo.utils.constants.Constants.URL_BACKEND
import org.override.apollo.utils.data.Student

class StudentService(private val client: HttpClient) {
    suspend fun getAll(courseId: String): StudentResponse {
        return try {
            client.get("${URL_BACKEND}student/all/$courseId").body<StudentResponse>()
        } catch (e: Exception) {
            StudentResponse(
                students = emptyList(),
                isSuccess = false,
                errorMessage = e.message ?: "Unknown error"
            )
        }
    }

    suspend fun add(student: Student, courseId: String): StudentResponse {
        return try {
            client.post {
                url("${URL_BACKEND}student/create")
                contentType(ContentType.Application.Json)
                setBody(StudentRequest(student = student, courseId = courseId))
            }.body<StudentResponse>()
        } catch (e: Exception) {
            StudentResponse(
                students = emptyList(),
                isSuccess = false,
                errorMessage = e.message
            )
        }
    }
}