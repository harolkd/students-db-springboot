package com.unisalle.backend.mapper

import com.unisalle.backend.dto.StudentCreateRequest
import com.unisalle.backend.dto.StudentResponse
import com.unisalle.backend.model.Student
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class StudentMapper {
    fun toEntity(request: StudentCreateRequest): Student {
        return Student(
            nombre = request.nombre?.trim() ?: "",
            apellido = request.apellido?.trim() ?: "",
            correoElectronico = request.correoElectronico?.trim()?.lowercase() ?: "",
            fechaCreacion = Instant.now()
        )
    }

    fun toResponse(student: Student): StudentResponse {
        return StudentResponse(
            id = student.id ?: throw IllegalStateException("Student ID cannot be null"),
            nombre = student.nombre,
            apellido = student.apellido,
            correoElectronico = student.correoElectronico,
            fechaCreacion = student.fechaCreacion
        )
    }
}
