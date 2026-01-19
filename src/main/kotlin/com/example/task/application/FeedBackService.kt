package com.example.task.application

import com.example.task.Interface.ChatThreadRepository
import com.example.task.Interface.FeedBackRepository
import com.example.task.Interface.UserRepository
import com.example.task.domain.FeedBackEntity
import com.example.task.domain.UserRole
import com.example.task.global.exeception.BusinessException
import com.example.task.global.exeception.ErrorCode
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class FeedBackService(
    private val feedBackRepository: FeedBackRepository,
    private val threadRepository: ChatThreadRepository,
    private val userRepository: UserRepository
) {

    fun createFeedback(
        userEmail: String,
        threadId: Long,
        positive: Boolean
    ): FeedBackEntity {

        val user = userRepository.findByEmail(userEmail)
            ?: throw BusinessException(ErrorCode.UNAUTHORIZED)

        val thread = threadRepository.findById(threadId)
            .orElseThrow { BusinessException(ErrorCode.THREAD_NOT_FOUND) }

        // 본인 스레드만 가능 (관리자는 예외)
        if (user.role != UserRole.ADMIN && thread.user.id != user.id) {
            throw BusinessException(ErrorCode.FORBIDDEN)
        }

        // 한 유저당 한 스레드에 하나의 피드백만 가능
        if (feedBackRepository.findByThreadAndUser(thread, user) != null) {
            throw BusinessException(ErrorCode.FEEDBACK_ALREADY_EXISTS)
        }

        val feedback = FeedBackEntity(
            thread = thread,
            user = user,
            positive = positive
        )

        return feedBackRepository.save(feedback)
    }

    @Transactional(readOnly = true)
    fun getFeedbacks(
        userEmail: String,
        threadId: Long,
        page: Int = 0,
        size: Int = 10,
        asc: Boolean = false,
        positive: Boolean? = null
    ): List<FeedBackEntity> {

        val user = userRepository.findByEmail(userEmail)
            ?: throw BusinessException(ErrorCode.UNAUTHORIZED)

        val thread = threadRepository.findById(threadId)
            .orElseThrow { BusinessException(ErrorCode.THREAD_NOT_FOUND) }

        val sort = if (asc) {
            Sort.by("createdAt").ascending()
        } else {
            Sort.by("createdAt").descending()
        }

        val pageable = PageRequest.of(page, size, sort)

        val pageResult = if (user.role == UserRole.ADMIN) {
            // 관리자: 모든 피드백 조회
            if (positive != null) {
                feedBackRepository.findAllByThreadAndPositive(thread, positive, pageable)
            } else {
                feedBackRepository.findAllByThread(thread, pageable)
            }
        } else {
            // 일반 사용자: 본인이 생성한 피드백만
            if (positive != null) {
                feedBackRepository.findAllByThreadAndUserAndPositive(thread, user, positive, pageable)
            } else {
                feedBackRepository.findAllByThreadAndUser(thread, user, pageable)
            }
        }

        return pageResult.content
    }

    fun updateFeedbackStatus(
        userEmail: String,
        feedbackId: Long,
        status: String
    ): FeedBackEntity {

        val user = userRepository.findByEmail(userEmail)
            ?: throw BusinessException(ErrorCode.UNAUTHORIZED)

        if (user.role != UserRole.ADMIN) {
            throw BusinessException(ErrorCode.FORBIDDEN)
        }

        val feedback = feedBackRepository.findById(feedbackId)
            .orElseThrow { BusinessException(ErrorCode.FEEDBACK_NOT_FOUND) }

        if (status != "pending" && status != "resolved") {
            throw BusinessException(ErrorCode.INVALID_INPUT)
        }

        feedback.status = status
        return feedBackRepository.save(feedback)
    }
}
