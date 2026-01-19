package com.example.task.presentation.dto

import com.example.task.domain.UserRole

data class AuthResponse(
    val email: String,
    val name: String,
    val role: UserRole,
    val accessToken: String
)