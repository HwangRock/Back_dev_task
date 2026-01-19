package com.example.task.application


import com.example.task.presentation.dto.ChatResult
import com.example.task.utils.strategy.AiCallStrategy
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val aiCallStrategy: AiCallStrategy
) {

    @CircuitBreaker(
        name = "openAiApi",
        fallbackMethod = "aiFallback"
    )
    fun callAi(
        prompt: String,
        isStreaming: Boolean,
        model: String?
    ): ChatResult {
        return if (isStreaming) {
            val emitter = aiCallStrategy.stream(prompt, model)
            ChatResult.Stream(emitter)
        } else {
            val answer = aiCallStrategy.call(prompt, model)
            ChatResult.Answer(answer)
        }
    }

    private fun aiFallback(
        prompt: String,
        isStreaming: Boolean,
        model: String?,
        ex: Throwable
    ): ChatResult {
        return ChatResult.Answer(
            "현재 AI 서버가 불안정합니다. 잠시 후 다시 시도해주세요."
        )
    }
}
