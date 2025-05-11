package com.example.danangforum.model

import jakarta.persistence.*

@Entity
@Table(name = "groupmember")
open class GroupMember(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,  // ID tự động tăng cho bảng groupmember

    @ManyToOne(fetch = FetchType.EAGER)  // Quan hệ với group
    @JoinColumn(name = "group_id", nullable = false)
    val group: Group,  // Nhóm mà thành viên tham gia

    @ManyToOne(fetch = FetchType.EAGER)  // Quan hệ với user
    @JoinColumn(name = "member_id", nullable = false)
    val member: User,  // Thành viên của nhóm

    @Enumerated(EnumType.STRING)
    var status: MemberStatus = MemberStatus.PENDING
){
    constructor() : this(0, Group(), User())
}

enum class MemberStatus {
    PENDING, APPROVED, REJECTED
}
