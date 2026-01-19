package com.example.task.utils.strategy

import com.example.task.utils.client.gemini.Content
import com.example.task.utils.client.gemini.GeminiFeignClient
import com.example.task.utils.client.gemini.GeminiRequest
import com.example.task.utils.client.gemini.Part
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@Profile("gemini")
@Component
class GeminiStrategy(
    private val geminiFeignClient: GeminiFeignClient,
    @Value("\${ai.gemini.api-key}")
    private val apiKey: String,
    @Value("\${ai.gemini.model}")
    private val model: String
) : AiCallStrategy {

    override fun call(prompt: String, model: String?): String {
        val request = GeminiRequest(
            contents = listOf(
                Content(parts = listOf(Part(text = prompt)))
            )
        )

        val usedModel = model ?: this.model

        val response = geminiFeignClient.generateContent(
            usedModel,
            apiKey,
            request
        )

        return response.candidates.first().content.parts.first().text
    }

    override fun stream(
        prompt: String,
        model: String?
    ): SseEmitter {

        val emitter = SseEmitter(60_000L) // 60초 타임아웃

        Thread {
            try {
                val fullResponse = call(prompt, model)

                fullResponse.split(" ").forEach { token ->
                    emitter.send(
                        SseEmitter.event()
                            .name("message")
                            .data(token)
                    )
                    Thread.sleep(80)
                }

                emitter.send(
                    SseEmitter.event()
                        .name("end")
                        .data("[DONE]")
                )
                emitter.complete()

            } catch (e: Exception) {
                emitter.completeWithError(e)
            }
        }.start()

        return emitter
    }

}