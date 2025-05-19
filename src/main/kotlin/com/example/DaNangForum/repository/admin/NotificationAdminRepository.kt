package com.example.DaNangForum.repository.admin

import com.example.danangforum.model.Notification
import org.springframework.data.jpa.repository.JpaRepository

interface NotificationAdminRepository: JpaRepository<Notification, Long>{
}