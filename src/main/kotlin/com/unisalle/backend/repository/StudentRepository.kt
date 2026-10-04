package com.unisalle.backend.repository

import com.unisalle.backend.model.Student
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface StudentRepository : JpaRepository<Student, UUID> {
    fun existsByCorreoElectronicoIgnoreCase(correoElectronico: String): Boolean
    fun findByCorreoElectronicoIgnoreCase(correoElectronico: String): Student?
}
