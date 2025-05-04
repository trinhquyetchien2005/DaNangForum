package com.example.danangforum.model

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "users")  // Đổi tên bảng thành "users" theo convention
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val user_id: Long = 0,

    @Column(unique = true)
    val username: String,

    val password: String? = null, // Nullable khi dùng OAuth

    @Column(unique = true)
    val email: String,

    val role: String,

    val phone: String,

    val school: String,

    val avatar: String,

    val bio: String,

    val dateOfBirth: LocalDate? = null,

    val address: String,

    @CreatedDate
    val create_at: LocalDateTime,

    @Enumerated(EnumType.STRING)
    val provider: AuthProvider = AuthProvider.LOCAL, // Google or local

    val enabled: Boolean = true // Để đánh dấu tài khoản đã được kích hoạt chưa
)

enum class AuthProvider {
    LOCAL, GOOGLE
}
