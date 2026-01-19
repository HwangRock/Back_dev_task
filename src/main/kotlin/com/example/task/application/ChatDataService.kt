package com.example.task.application

import com.example.task.Interface.ChatThreadRepository
import com.example.task.Interface.UserRepository
import com.example.task.domain.ChatMessageEntity
import com.example.task.domain.ChatRole
import com.example.task.domain.ChatThreadEntity
import com.example.task.domain.UserRole
import com.example.task.domain.repository.ChatMessageRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
@Transactional
class ChatDataService(
    private val threadRepository: ChatThreadRepository,
    private val messageRepository: ChatMessageRepository,
    private val userRepository: UserRepository
) {

    // 기존 AI 관련 함수 그대로
    fun getOrCreateThread(email: String): ChatThreadEntity {
        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("User not found")

        val threshold = LocalDateTime.now().minusMinutes(30)

        return threadRepository.findActiveThread(user, threshold)
            ?: threadRepository.save(
                ChatThreadEntity(user = user)
            )
    }

    fun buildContextPrompt(
        thread: ChatThreadEntity,
        prompt: String
    ): String {
        val history = thread.chats.joinToString("\n") {
            "${it.role}: ${it.content}"
        }

        return if (history.isBlank()) prompt
        else "$history\nUSER: $prompt"
    }

    fun saveChat(
        thread: ChatThreadEntity,
        prompt: String,
        answer: String
    ) {
        messageRepository.save(
            ChatMessageEntity(
                thread = thread,
                content = prompt,
                role = ChatRole.USER
            )
        )

        messageRepository.save(
            ChatMessageEntity(
                thread = thread,
                content = answer,
                role = ChatRole.ASSISTANT
            )
        )

        thread.lastQuestionAt = LocalDateTime.now()
    }

    fun getUserThreads(
        email: String,
        isAdmin: Boolean = false,
        page: Int = 0,
        size: Int = 10,
        asc: Boolean = false
    ): List<ChatThreadEntity> {
        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("User not found")

        val sort = if (asc) Sort.by("createdAt").ascending()
        else Sort.by("createdAt").descending()
        val pageable = PageRequest.of(page, size, sort)

        return if (isAdmin && user.role == UserRole.ADMIN) {
            threadRepository.findAll(pageable).content
        } else {
            threadRepository.findByUser(user, pageable).content
        }
    }

    fun deleteThread(email: String, threadId: Long) {
        val thread = threadRepository.findById(threadId)
            .orElseThrow { IllegalArgumentException("Thread not found") }

        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("User not found")

        // ADMIN은 모든 스레드 삭제 가능, MEMBER는 자신 소유만 가능
        if (user.role != UserRole.ADMIN && thread.user.id != user.id) {
            throw IllegalAccessException("Not authorized to delete this thread")
        }

        threadRepository.delete(thread)
    }
}