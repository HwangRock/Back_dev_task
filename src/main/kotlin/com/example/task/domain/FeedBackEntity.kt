package com.example.task.domain

import jakarta.persistence.*

@Entity
@Table(
    name = "feedback",
    uniqueConstraints = [UniqueConstraint(columnNames = ["thread_id", "user_id"])]
)
class FeedBackEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_id", nullable = false)
    val thread: ChatThreadEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    val positive: Boolean, // true = 긍정, false = 부정

    @Column(nullable = false)
    var status: String = "pending" // pending / resolved

) : BaseEntity()
