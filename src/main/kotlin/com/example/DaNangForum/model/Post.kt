package com.example.danangforum.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "post")
data class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val postId: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    val group: Group? = null,

    @Column(columnDefinition = "TEXT", nullable = false)
    var content: String = "",

    var image: String? = null,

    var video: String? = null,

    val createAt: LocalDateTime = LocalDateTime.now()
) {
    // If Hibernate requires a no-argument constructor, Kotlin's default constructor should suffice
    constructor() : this(0, User(), null, "", null, null, LocalDateTime.now())
}
