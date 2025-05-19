package com.example.danangforum.model

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "user")  // Đổi tên bảng thành "users" theo convention
open class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userId: Long = 0,


    var username: String,

    var password: String? = null, // Nullable khi dùng OAuth

    @Column(unique = true)
    val email: String,

    var role: String,

    var school: String,

    var avatar: String,

    val phoneNumber: String,

    var bio: String,

    var dateOfBirth: LocalDate? = null,

    var address: String,

    @CreatedDate
    val create_at: LocalDateTime,

    @Enumerated(EnumType.STRING)
    val provider: AuthProvider = AuthProvider.LOCAL, // Google or local
){
    constructor() : this(
        0, "", null, "", "", "", "", "", "", null, "", LocalDateTime.now(), AuthProvider.LOCAL
    )
}

enum class AuthProvider {
    LOCAL, GOOGLE
}
