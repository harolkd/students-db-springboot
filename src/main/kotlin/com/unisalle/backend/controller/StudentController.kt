package com.unisalle.backend.controller

import com.unisalle.backend.dto.StudentCreateRequest
import com.unisalle.backend.dto.StudentResponse
import com.unisalle.backend.service.StudentService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.util.UUID

@RestController
@RequestMapping("/api/v1/students")
class StudentController(
    private val studentService: StudentService
) {

    @PostMapping
    fun createStudent(@RequestBody request: StudentCreateRequest): ResponseEntity<StudentResponse> {
        val created = studentService.create(request)
        val location: URI = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(created.id)
            .toUri()
        return ResponseEntity.created(location).body(created)
    }

    @GetMapping("/{id}")
    fun getStudentById(@PathVariable id: UUID): ResponseEntity<StudentResponse> {
        val student = studentService.findById(id)
        return ResponseEntity.ok(student)
    }

    @GetMapping
    fun listStudents(
        @RequestParam(required = false) limit: Int?,
        @RequestParam(required = false) offset: Int?
    ): ResponseEntity<List<StudentResponse>> {
        val students = studentService.findAll(limit, offset)
        return ResponseEntity.ok(students)
    }
}
