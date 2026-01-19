package com.example.task.presentation.dto

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

sealed class ChatResult {

    data class Answer(
        val answer: String
    ) : ChatResult()

    data class Stream(
        val emitter: SseEmitter
    ) : ChatResult()
}
