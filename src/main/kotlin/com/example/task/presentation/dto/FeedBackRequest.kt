package com.example.task.presentation.dto

data class FeedBackRequest(
    val threadId: Long,
    val positive: Boolean
)