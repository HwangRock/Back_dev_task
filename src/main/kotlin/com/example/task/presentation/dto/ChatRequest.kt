package com.example.task.presentation.dto

import jakarta.validation.constraints.NotBlank

data class ChatRequest(
    @field:NotBlank(message = "prompt must not be blank")
    val prompt: String,
    val isStreaming: Boolean = false,
    val model: String? = null
)
