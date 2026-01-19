package com.example.task.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

@Entity
@Table(name = "chat_message")
class ChatMessageEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_id", nullable = false)
    @JsonBackReference
    val thread: ChatThreadEntity,

    @Column(nullable = false, columnDefinition = "TEXT")
    val content: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val role: ChatRole

) : BaseEntity()

enum class ChatRole {
    USER, ASSISTANT
}
