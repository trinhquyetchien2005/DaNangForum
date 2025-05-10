package com.example.danangforum.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "comment")
open class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val commentId: Long = 0,  // ID tự động tăng cho comment

    @ManyToOne(fetch = FetchType.LAZY)  // Quan hệ với bài viết (post)
    @JoinColumn(name = "post_id", nullable = false)
    val post: Post?,  // Bài viết mà comment thuộc về

    @ManyToOne(fetch = FetchType.LAZY)  // Quan hệ với người dùng (user)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User?,  // Người viết comment

    @Column(name = "content", nullable = false)
    var content: String,  // Nội dung comment

    @Column(name = "create_at", updatable = false)
    val createAt: LocalDateTime = LocalDateTime.now()  // Thời gian tạo comment
){
    constructor() : this(0, null, null, "")
}
