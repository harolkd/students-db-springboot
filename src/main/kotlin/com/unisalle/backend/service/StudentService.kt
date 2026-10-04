package com.unisalle.backend.service

import com.unisalle.backend.dto.StudentCreateRequest
import com.unisalle.backend.dto.StudentResponse
import com.unisalle.backend.mapper.StudentMapper
import com.unisalle.backend.model.Student
import com.unisalle.backend.repository.StudentRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.UUID
import java.util.regex.Pattern

@Service
class StudentService(
    private val studentRepository: StudentRepository,
    private val studentMapper: StudentMapper
) {

    private val emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

    fun create(request: StudentCreateRequest): StudentResponse {
        val nombre = request.nombre?.trim()
        val apellido = request.apellido?.trim()
        val correo = request.correoElectronico?.trim()?.lowercase()

        if (nombre.isNullOrEmpty() || nombre.length > 100) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio y debe tener entre 1 y 100 caracteres")
        }

        if (apellido.isNullOrEmpty() || apellido.length > 100) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "El apellido es obligatorio y debe tener entre 1 y 100 caracteres")
        }

        if (correo.isNullOrEmpty() || !emailPattern.matcher(correo).matches()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo electrónico es obligatorio y debe ser válido")
        }

        if (studentRepository.existsByCorreoElectronicoIgnoreCase(correo)) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "El correo electrónico ya se encuentra registrado")
        }

        val student = Student(
            nombre = nombre,
            apellido = apellido,
            correoElectronico = correo
        )

        val saved = studentRepository.save(student)
        return studentMapper.toResponse(saved)
    }

    fun findById(id: UUID): StudentResponse {
        val student = studentRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado")
        }
        return studentMapper.toResponse(student)
    }

    fun findAll(limit: Int?, offset: Int?): List<StudentResponse> {
        val size = limit?.coerceAtLeast(1) ?: 20
        val page = (offset?.coerceAtLeast(0) ?: 0) / size
        val pageable: Pageable = PageRequest.of(page, size)
        return studentRepository.findAll(pageable).content.map { studentMapper.toResponse(it) }
    }
}
