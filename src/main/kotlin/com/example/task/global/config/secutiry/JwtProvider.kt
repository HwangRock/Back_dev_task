package com.example.task.global.config.secutiry

import com.example.task.domain.UserRole
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtProvider(

    @Value("\${jwt.secret}")
    private val secret: String,

    @Value("\${jwt.access-token-expiration}")
    private val accessTokenExpiration: Long
) {
    private val key: SecretKey =
        Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))

    fun createToken(email: String, role: UserRole): String {
        val now = Date()
        val validity = Date(now.time + accessTokenExpiration)

        return Jwts.builder()
            .subject(email)
            .claim("role", role.name)
            .issuedAt(now)
            .expiration(validity)
            .signWith(key)
            .compact()
    }

    fun getEmail(token: String): String =
        getClaims(token).subject

    fun getRole(token: String): UserRole {
        val roleStr = getClaims(token).get("role", String::class.java)
        return UserRole.valueOf(roleStr)
    }

    fun validateToken(token: String): Boolean =
        try {
            !getClaims(token).expiration.before(Date())
        } catch (e: Exception) {
            false
        }

    private fun getClaims(token: String): Claims =
        Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
}
