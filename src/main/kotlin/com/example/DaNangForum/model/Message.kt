package com.example.danangforum.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "message")
open class Message(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val messageId: Long = 0,  // ID tự động tăng cho bảng message

    @ManyToOne(fetch = FetchType.EAGER)  // Quan hệ với user (người gửi)
    @JoinColumn(name = "sender_id", nullable = false)
    val sender: User,  // Người gửi

    @ManyToOne(fetch = FetchType.EAGER)  // Quan hệ với user (người nhận)
    @JoinColumn(name = "receiver_id")
    val receiver: User?,  // Người nhận, có thể là null

    @Lob  // Lưu trữ nội dung tin nhắn dưới dạng văn bản dài
    @Column(name = "content", columnDefinition = "TEXT")
    val content: String?,  // Nội dung tin nhắn (có thể null)

    @Column(name = "image")
    val image: String?,  // Đường dẫn đến ảnh đính kèm (có thể null)

    @Column(name = "video")
    val video: String?,  // Đường dẫn đến video đính kèm (có thể null)

    @Column(name = "create_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    val createAt: java.time.LocalDateTime = java.time.LocalDateTime.now()  // Thời gian tạo tin nhắn
) {
    constructor() : this(0, User(), null, null, null, null, LocalDateTime.now())
}
