package com.example.danangforum.model

import jakarta.persistence.*

@Entity
@Table(name = "groupchatmember")
open class GroupChatMember(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,  // ID tự động tăng cho bảng groupchatmember

    @ManyToOne(fetch = FetchType.LAZY)  // Quan hệ với group chat
    @JoinColumn(name = "group_id", nullable = false)
    val groupChat: GroupChat,  // Nhóm chat mà thành viên tham gia

    @ManyToOne(fetch = FetchType.LAZY)  // Quan hệ với user
    @JoinColumn(name = "member_id", nullable = false)
    val member: User  // Thành viên của nhóm chat
){
    constructor() : this(0, GroupChat(), User())
}
