package com.unisalle.backend.config

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

import java.util.Arrays
import java.util.List

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableWebSecurity
class SecurityConfig {
    init {
        println("SecurityConfig initialized!")
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        println("Building security filter chain...")
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth.anyRequest().permitAll()
            }
            .httpBasic { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
        return http.build()
    }

    @Bean
    fun corsConfigurationSource():CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            allowedOrigins = listOf("http://localhost:8080")
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
            allowedHeaders = listOf("*")
            allowCredentials = true
        }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
    