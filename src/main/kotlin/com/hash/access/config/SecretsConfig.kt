package com.hash.access.config

import com.nimbusds.jose.jwk.source.ImmutableSecret
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


@Configuration
class SecretsConfig {

    @Value("\${app.code.secret}")
    lateinit var secret: String

    @Bean
    fun jwtDecoder(): JwtDecoder {
        val originalKey: SecretKey = SecretKeySpec(secret.toByteArray(), "HmacSHA256")
        return NimbusJwtDecoder.withSecretKey(originalKey).build()
    }

    @Bean
    fun jwtEncoder(): JwtEncoder {
        val key: SecretKey = SecretKeySpec(secret.toByteArray(), "HmacSHA256")
        val immutableSecret: JWKSource<SecurityContext> = ImmutableSecret(key)
        return NimbusJwtEncoder(immutableSecret)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
