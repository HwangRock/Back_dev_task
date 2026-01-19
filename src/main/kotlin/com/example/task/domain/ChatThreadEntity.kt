package com.example.task.domain

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "chat_thread",
    indexes = [
        Index(name = "idx_chat_thread_user_last", columnList = "user_id,lastQuestionAt")
    ]
)
class ChatThreadEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    var lastQuestionAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(
        mappedBy = "thread",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @JsonManagedReference
    val chats: MutableList<ChatMessageEntity> = mutableListOf()

) : BaseEntity()
