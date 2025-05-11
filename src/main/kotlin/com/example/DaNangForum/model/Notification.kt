package com.example.danangforum.model

import jakarta.persistence.*

@Entity
@Table(name = "notification")
open class Notification(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val notificationId: Long = 0,  // ID tự động tăng cho bảng notification

    @ManyToOne(fetch = FetchType.EAGER)  // Quan hệ với user (người nhận thông báo)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,  // Người nhận thông báo

    @Lob  // Lưu trữ nội dung thông báo dưới dạng văn bản dài
    @Column(name = "content")
    val content: String,  // Nội dung thông báo

    @Column(name = "type", nullable = false)
    val type: String  // Loại thông báo (ví dụ: "like", "comment", "follow", etc.)
) {
    constructor() : this(0, User(), "", "")}
