package com.unisalle.backend.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.sql.DataSource

@RestController
@RequestMapping("/api/v1/health")
class HealthController(
    private val dataSource: DataSource
) {

    @GetMapping
    fun healthCheck(): ResponseEntity<Map<String, String>> {
        return try {
            dataSource.connection.use { connection ->
                if (connection.isValid(2)) {
                    ResponseEntity.ok(mapOf("status" to "UP"))
                } else {
                    ResponseEntity.status(503).body(mapOf("status" to "DOWN", "error" to "Database connection is not valid"))
                }
            }
        } catch (ex: Exception) {
            ResponseEntity.status(503).body(mapOf("status" to "DOWN", "error" to (ex.message ?: "Database unavailable")))
        }
    }
}
