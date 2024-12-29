package com.hash.access.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.hash.access.model.Response
import com.hash.access.service.SecurityUserDetailsService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.nio.charset.StandardCharsets

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val userService: SecurityUserDetailsService
) {

    @Value("\${allow.origin.url}")
    lateinit var origin: String

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .cors { }
            .authorizeHttpRequests {
                it.requestMatchers("/user/register","/api/user/register").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2ResourceServer { it.jwt(Customizer.withDefaults()) }
            .userDetailsService(userService)
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .httpBasic { }
            .exceptionHandling {
                it.authenticationEntryPoint(CustomAuthenticationEntryPoint())
            }
            .build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf(origin)
        configuration.allowedMethods = listOf(origin)
        configuration.allowedHeaders = listOf(origin)
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    private class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {
        override fun commence(
            request: HttpServletRequest,
            response: HttpServletResponse,
            authException: AuthenticationException
        ) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            val errorResponse = Response(data = emptyList<Any>(),status = HttpStatus.UNAUTHORIZED.value(),HttpStatus.UNAUTHORIZED.name)
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.characterEncoding = StandardCharsets.UTF_8.toString()
            response.writer.write(ObjectMapper().writeValueAsString(errorResponse))
        }
    }
}
