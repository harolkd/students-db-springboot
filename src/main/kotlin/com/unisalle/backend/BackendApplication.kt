package com.unisalle.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BackendApplication

fun main(args: Array<String>) {
	val context = runApplication<BackendApplication>(*args)
	val port = context.environment.getProperty("server.port", "8080")
	println("Server is running on port $port")
}
