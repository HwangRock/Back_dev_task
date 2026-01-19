package com.example.task.facade

import com.example.task.application.ChatDataService
import com.example.task.application.ChatService
import com.example.task.domain.ChatThreadEntity
import com.example.task.presentation.dto.ChatRequest
import com.example.task.presentation.dto.ChatResult
import org.springframework.stereotype.Component

@Component
class ChatFacade(
    private val chatService: ChatService,
    private val chatDataService: ChatDataService
) {

    fun execute(email: String, request: ChatRequest): ChatResult {

        val thread = chatDataService.getOrCreateThread(email)

        val contextPrompt = chatDataService.buildContextPrompt(
            thread = thread,
            prompt = request.prompt
        )

        val result = chatService.callAi(
            prompt = contextPrompt,
            isStreaming = request.isStreaming,
            model = request.model
        )

        if (result is ChatResult.Answer) {
            chatDataService.saveChat(
                thread = thread,
                prompt = request.prompt,
                answer = result.answer
            )
        }

        return result
    }

    fun getThreads(
        email: String,
        isAdmin: Boolean = false,
        page: Int = 0,
        size: Int = 10,
        asc: Boolean = false
    ): List<ChatThreadEntity> {
        return chatDataService.getUserThreads(email, isAdmin, page, size, asc)
    }




    fun deleteThread(email: String, threadId: Long) =
        chatDataService.deleteThread(email, threadId)
}
