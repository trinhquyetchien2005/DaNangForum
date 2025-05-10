package com.example.danangforum.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "block")
open class Block(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,  // ID tự động tăng

    @ManyToOne(fetch = FetchType.LAZY)  // Quan hệ với người chặn (blocker)
    @JoinColumn(name = "blocker_id", nullable = false)
    val blocker: User,  // Người chặn

    @ManyToOne(fetch = FetchType.LAZY)  // Quan hệ với người bị chặn (blocked)
    @JoinColumn(name = "blocked_id", nullable = false)
    val blocked: User,  // Người bị chặn

){
    constructor() : this(0, User(), User())
}
