package com.example.danangforum.model

import jakarta.persistence.*

@Entity
@Table(name = "groupmember")
data class GroupMember(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,  // ID tự động tăng cho bảng groupmember

    @ManyToOne(fetch = FetchType.LAZY)  // Quan hệ với group
    @JoinColumn(name = "group_id", nullable = false)
    val group: Group,  // Nhóm mà thành viên tham gia

    @ManyToOne(fetch = FetchType.LAZY)  // Quan hệ với user
    @JoinColumn(name = "member_id", nullable = false)
    val member: User  // Thành viên của nhóm
)
