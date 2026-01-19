package com.example.task.application.admin

import com.example.task.domain.repository.ChatMessageRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AdminReportService(
    private val chatMessageRepository: ChatMessageRepository
) {

    fun generateDailyCsv(): String {
        val from = LocalDateTime.now().minusDays(1)
        val messages = chatMessageRepository.findAllForDailyReport(from)

        return buildString {
            appendLine("messageId,userId,email,threadId,role,content,createdAt")
            messages.forEach { m ->
                appendLine(
                    "${m.id}," +
                            "${m.thread.user.id}," +
                            "${m.thread.user.email}," +
                            "${m.thread.id}," +
                            "${m.role}," +
                            "\"${m.content.replace("\"", "\"\"")}\"," +
                            "${m.createdAt}"
                )
            }
        }
    }
}
