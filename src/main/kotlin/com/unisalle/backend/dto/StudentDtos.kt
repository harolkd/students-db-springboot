package com.unisalle.backend.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant
import java.util.UUID

data class StudentCreateRequest(
    val nombre: String? = null,
    val apellido: String? = null,
    @JsonProperty("correo_electronico")
    val correoElectronico: String? = null
)

data class StudentResponse(
    val id: UUID,
    val nombre: String,
    val apellido: String,
    @JsonProperty("correo_electronico")
    val correoElectronico: String,
    @JsonProperty("fecha_creacion")
    val fechaCreacion: Instant
)
