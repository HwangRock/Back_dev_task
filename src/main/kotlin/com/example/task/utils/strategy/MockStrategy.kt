package com.example.task.utils.strategy

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@Profile("Test")
@Component
class MockAiCallStrategy : AiCallStrategy {

    override fun call(prompt: String, model: String?): String {
        return "AI 응답입니다."
    }

    override fun stream(prompt: String, model: String?): SseEmitter {
        val emitter = SseEmitter()

        Thread {
            try {
                emitter.send("AI 응답 1")
                Thread.sleep(500)
                emitter.send("AI 응답 2")
                Thread.sleep(500)
                emitter.complete()
            } catch (e: Exception) {
                emitter.completeWithError(e)
            }
        }.start()

        return emitter
    }
}
