package com.example.danangforum.model

import jakarta.persistence.*

@Entity
@Table(name = "follower")
data class Follower(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,  // ID tự động tăng cho bảng follower

    @ManyToOne(fetch = FetchType.LAZY)  // Quan hệ với người theo dõi (follower)
    @JoinColumn(name = "follower_id", nullable = false)
    val follower: User,  // Người theo dõi

    @ManyToOne(fetch = FetchType.LAZY)  // Quan hệ với người được theo dõi (following)
    @JoinColumn(name = "following_id", nullable = false)
    val following: User  // Người được theo dõi
)
