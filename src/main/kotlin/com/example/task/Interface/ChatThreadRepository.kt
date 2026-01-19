package com.example.task.Interface

import com.example.task.domain.ChatThreadEntity
import com.example.task.domain.User
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime


interface ChatThreadRepository : JpaRepository<ChatThreadEntity, Long> {

    @Query("""
        select t
        from ChatThreadEntity t
        where t.user = :user
          and t.lastQuestionAt >= :threshold
        order by t.lastQuestionAt desc
    """)
    fun findActiveThread(
        user: User,
        threshold: LocalDateTime
    ): ChatThreadEntity?

    fun findByUser(user: User): List<ChatThreadEntity>


    fun findByUser(user: User, pageable: Pageable): Page<ChatThreadEntity>

    @Query("""
        SELECT COUNT(t)
        FROM ChatThreadEntity t
        WHERE t.createdAt >= :from
    """)
    fun countCreatedSince(from: LocalDateTime): Long
}
