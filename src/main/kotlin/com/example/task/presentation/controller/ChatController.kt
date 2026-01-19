package com.example.task.presentation.controller

import com.example.task.facade.ChatFacade
import com.example.task.global.response.ApiResponse
import com.example.task.presentation.dto.ChatRequest
import com.example.task.presentation.dto.ChatResponse
import com.example.task.presentation.dto.ChatResult
import com.example.task.domain.ChatThreadEntity
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@Tag(name = "Chat", description = "대화 관리 API")
@RestController
@RequestMapping("/api/chats")
class ChatController(
    private val chatFacade: ChatFacade
) {

    @Operation(
        summary = "대화 생성",
        description = "AI에게 질문을 보내고 답변을 생성합니다. 30분 이내 질문 시 기존 스레드가 유지됩니다."
    )
    @PostMapping
    fun create(
        @AuthenticationPrincipal email: String,
        @Valid @RequestBody request: ChatRequest
    ): Any {

        val result = chatFacade.execute(email, request)

        return when (result) {
            is ChatResult.Answer ->
                ApiResponse.success(ChatResponse(result.answer))
            is ChatResult.Stream ->
                result.emitter
        }
    }

    @GetMapping
    fun getThreads(
        @AuthenticationPrincipal email: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "false") asc: Boolean,
        @RequestParam(defaultValue = "false") isAdmin: Boolean
    ): ApiResponse<List<ChatThreadEntity>> {

        val threads = chatFacade.getThreads(email, isAdmin, page, size, asc)
        return ApiResponse.success(threads)
    }



    @DeleteMapping("/{threadId}")
    fun deleteThread(
        @AuthenticationPrincipal email: String,
        @PathVariable threadId: Long
    ): ApiResponse<String> {

        chatFacade.deleteThread(email, threadId)
        return ApiResponse.success("Thread deleted successfully")
    }
}
