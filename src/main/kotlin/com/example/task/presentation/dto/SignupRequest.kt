package com.example.task.presentation.dto

import com.example.task.domain.UserRole
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class SignupRequest(
    @field:Email @field:NotBlank val email: String,
    @field:NotBlank @field:Size(min = 4) val password: String,
    @field:NotBlank val name: String,
    val role: UserRole = UserRole.MEMBER
)
