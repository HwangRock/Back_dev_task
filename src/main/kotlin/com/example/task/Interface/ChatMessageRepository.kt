package com.example.task.domain.repository

import com.example.task.domain.ChatMessageEntity
import com.example.task.domain.ChatThreadEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface ChatMessageRepository : JpaRepository<ChatMessageEntity, Long> {

    fun findByThread(
        thread: ChatThreadEntity,
        pageable: Pageable
    ): Page<ChatMessageEntity>

    @Query("""
        SELECT m
        FROM ChatMessageEntity m
        JOIN FETCH m.thread t
        JOIN FETCH t.user u
        WHERE m.createdAt >= :from
        ORDER BY u.id, t.id, m.createdAt
    """)
    fun findAllForDailyReport(from: LocalDateTime): List<ChatMessageEntity>
}
