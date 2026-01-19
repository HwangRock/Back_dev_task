package com.example.task.Interface

import com.example.task.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean

    @Query("""
        SELECT COUNT(u)
        FROM User u
        WHERE u.createdAt >= :from
    """)
    fun countSignupSince(from: LocalDateTime): Long
}