package com.example.danangforum.model

import jakarta.persistence.*

@Entity
@Table(name = "like")
data class Like(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val likeId: Long = 0,  // ID tự động tăng cho bảng like

    @ManyToOne(fetch = FetchType.LAZY)  // Quan hệ với post
    @JoinColumn(name = "post_id", nullable = false)
    val post: Post,  // Bài viết mà người dùng thích

    @ManyToOne(fetch = FetchType.LAZY)  // Quan hệ với user
    @JoinColumn(name = "user_id", nullable = false)
    val user: User  // Người dùng đã thích bài viết
)
