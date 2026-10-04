package com.unisalle.backend.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "students")
class Student(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,

    @Column(name = "nombre", nullable = false, length = 100)
    var nombre: String = "",

    @Column(name = "apellido", nullable = false, length = 100)
    var apellido: String = "",

    @Column(name = "correo_electronico", nullable = false, unique = true, length = 255)
    var correoElectronico: String = "",

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    var fechaCreacion: Instant = Instant.now()
) {
    @PrePersist
    fun onPrePersist() {
        if (fechaCreacion == null) {
            fechaCreacion = Instant.now()
        }
    }
}
