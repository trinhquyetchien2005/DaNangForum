package com.example.danangforum.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "groupchat")
open class GroupChat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val groupchat_id: Long = 0,  // ID tự động tăng cho bảng groupchat

    @ManyToOne(fetch = FetchType.LAZY)  // Quan hệ với người sở hữu group chat
    @JoinColumn(name = "owner_id", nullable = false)
    val owner: User,  // Chủ sở hữu groupchat

    @Column(name = "groupname", length = 100)
    val groupname: String,  // Tên nhóm chat

    @Column(name = "image", length = 255)
    val image: String? = null,  // Hình ảnh đại diện cho nhóm chat (nếu có)

    @Column(name = "create_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    val create_at: LocalDateTime = LocalDateTime.now()  // Thời gian tạo group chat
){
    constructor() : this(0, User(), "", null, LocalDateTime.now())
}
