package com.example.task.application.admin

import com.example.task.Interface.ChatThreadRepository
import com.example.task.Interface.UserRepository
import com.example.task.infra.memory.LoginCounter
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AdminActivityService(
    private val userRepository: UserRepository,
    private val chatThreadRepository: ChatThreadRepository
) {

    fun getDailyStats(): DailyActivityResult {
        val from = LocalDateTime.now().minusDays(1)

        return DailyActivityResult(
            signupCount = userRepository.countSignupSince(from),
            loginCount = LoginCounter.getTodayCount(),
            chatCreatedCount = chatThreadRepository.countCreatedSince(from)
        )
    }
}

data class DailyActivityResult(
    val signupCount: Long,
    val loginCount: Int,
    val chatCreatedCount: Long
)
