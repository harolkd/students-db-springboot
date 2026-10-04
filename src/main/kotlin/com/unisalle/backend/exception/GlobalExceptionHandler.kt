package com.unisalle.backend.exception;

import java.time.Instant
import java.util.HashMap
import kotlin.collections.Map;
import java.time.LocalDateTime

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController

@RestController
class GlobalExceptionHandler

@ExceptionHandler(ResourceNotFoundException::class)
fun handleNotFound(ex: ResourceNotFoundException): ResponseEntity<Map<String, Any>> {
    val body = mapOf<String, Any>(
        "timestamp" to LocalDateTime.now(),
        "status" to HttpStatus.NOT_FOUND.toString(),
        "error" to ("Not Found"),
        "Message" to (ex.message ?: "Resource not found")
    )

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body)
}