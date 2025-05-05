package com.example.danangforum.model

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "user")  // Đổi tên bảng thành "users" theo convention
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val user_id: Long = 0,

    @Column(unique = true)
    val username: String,

    var password: String? = null, // Nullable khi dùng OAuth

    @Column(unique = true)
    val email: String,

    val role: String,

    val school: String,

    val avatar: String,

    val phoneNumber: String,

    val bio: String,

    val dateOfBirth: LocalDate? = null,

    val address: String,

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
