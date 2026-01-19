package com.example.task.utils.strategy

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

interface AiCallStrategy {

    fun call(
        prompt: String,
        model: String? = null
    ): String

    fun stream(
        prompt: String,
        model: String? = null
    ): SseEmitter
}