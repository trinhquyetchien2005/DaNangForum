package com.example.DaNangForum.repository.admin

import com.example.danangforum.model.User
import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import java.time.LocalDateTime

interface UserAdminRepository: JpaRepository<User, Long> {
        @Query("""
       SELECT COUNT(u)
       FROM User u
       WHERE FUNCTION('DATE', u.create_at) = :date
    """)
        fun countByDate(@Param("date") date: LocalDate): Long

        fun findByEmail(@Param("email") email: String): User?
}
