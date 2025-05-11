package com.example.danangforum.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "`group`") // dùng ` để tránh lỗi vì "group" là từ khóa SQL
open class Group(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val groupId: Long = 0,

    @Column(nullable = false)
    var groupname: String = "",  // Đảm bảo groupname không phải null

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false)
    val owner: User,  // Hibernate sẽ gán owner sau

    @Column(name = "create_at")
    val createAt: LocalDateTime = LocalDateTime.now()
) {
    // Constructor mặc định cho Hibernate (không thể để owner = 0 vì là một entity)
    constructor() : this(0, "", User(), LocalDateTime.now())
}

