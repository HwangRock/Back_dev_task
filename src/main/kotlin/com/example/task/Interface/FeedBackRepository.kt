package com.example.task.Interface

import com.example.task.domain.ChatThreadEntity
import com.example.task.domain.FeedBackEntity
import com.example.task.domain.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FeedBackRepository : JpaRepository<FeedBackEntity, Long> {

    fun findByThreadAndUser(
        thread: ChatThreadEntity,
        user: User
    ): FeedBackEntity?

    fun findAllByThread(
        thread: ChatThreadEntity,
        pageable: Pageable
    ): Page<FeedBackEntity>

    fun findAllByThreadAndPositive(
        thread: ChatThreadEntity,
        positive: Boolean,
        pageable: Pageable
    ): Page<FeedBackEntity>

    fun findAllByThreadAndUser(
        thread: ChatThreadEntity,
        user: User,
        pageable: Pageable
    ): Page<FeedBackEntity>

    fun findAllByThreadAndUserAndPositive(
        thread: ChatThreadEntity,
        user: User,
        positive: Boolean,
        pageable: Pageable
    ): Page<FeedBackEntity>
}
