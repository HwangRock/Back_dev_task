package com.example.task.presentation.controller

import com.example.task.application.FeedBackService
import com.example.task.domain.FeedBackEntity
import com.example.task.global.response.ApiResponse
import com.example.task.presentation.dto.FeedBackRequest
import com.example.task.presentation.dto.FeedBackStatusRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/feedbacks")
class FeedBackController(
    private val feedBackService: FeedBackService
) {

    @PostMapping
    fun createFeedback(
        @AuthenticationPrincipal email: String,
        @RequestBody request: FeedBackRequest
    ): ApiResponse<FeedBackEntity> {
        val feedback = feedBackService.createFeedback(
            userEmail = email,
            threadId = request.threadId,
            positive = request.positive
        )
        return ApiResponse.success(feedback)
    }

    @GetMapping("/{threadId}")
    fun getFeedbacks(
        @AuthenticationPrincipal email: String,
        @PathVariable threadId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "false") asc: Boolean,
        @RequestParam(required = false) positive: Boolean?
    ): ApiResponse<List<FeedBackEntity>> {

        val feedbacks = feedBackService.getFeedbacks(
            userEmail = email,
            threadId = threadId,
            page = page,
            size = size,
            asc = asc,
            positive = positive
        )

        return ApiResponse.success(feedbacks)
    }

    @PatchMapping("/{feedbackId}/status")
    fun updateFeedbackStatus(
        @AuthenticationPrincipal email: String,
        @PathVariable feedbackId: Long,
        @RequestBody request: FeedBackStatusRequest
    ): ApiResponse<FeedBackEntity> {

        val feedback = feedBackService.updateFeedbackStatus(
            userEmail = email,
            feedbackId = feedbackId,
            status = request.status
        )

        return ApiResponse.success(feedback)
    }

}
