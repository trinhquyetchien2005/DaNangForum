package com.example.DaNangForum.dto.admin

import com.example.danangforum.model.Notification
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

data class NotificationDTO(
    val content: String,
    val createdAt: LocalDateTime,
)