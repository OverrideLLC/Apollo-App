package org.override.apollo.domain.repositories

import dev.shreyaspatil.ai.client.generativeai.common.util.Log
import org.override.apollo.domain.response.StudentResponse
import org.override.apollo.domain.service.StudentService
import org.override.apollo.utils.data.Student

class StudentRepository(private val studentService: StudentService) {
    suspend fun getAllStudents(courseId: String): List<Student> {
        return try {
            Log.w("StudentRepository", "Fetching students from API")
            studentService.getAll(courseId).students ?: emptyList()
        } catch (e: Exception) {
            Log.e("StudentRepository", "Error fetching students", e)
            emptyList()
        }
    }

    suspend fun addStudent(student: Student, courseId: String): StudentResponse {
        try {
            Log.w("StudentRepository", "Adding student to API")
            return studentService.add(student, courseId)
        } catch (e: Exception) {
            Log.e("StudentRepository", "Error adding student", e)
            return StudentResponse(
                students = emptyList(),
                isSuccess = false,
                errorMessage = e.message
            )
        }
    }
}