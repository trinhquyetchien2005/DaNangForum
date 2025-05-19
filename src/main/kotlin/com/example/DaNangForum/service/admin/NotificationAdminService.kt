package com.example.DaNangForum.service.admin

import com.example.DaNangForum.repository.admin.NotificationAdminRepository
import com.example.danangforum.model.Notification
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class NotificationAdminService(
    private val notificationAdminRepository: NotificationAdminRepository
) {
    fun create(notification: Notification): ResponseEntity<Notification> {
        notificationAdminRepository.save(notification)
        return ResponseEntity.status(200).body(notification)
    }

    fun findAll(): ResponseEntity<List<Notification>> {
        return ResponseEntity.status(200).body(notificationAdminRepository.findAll())
    }

    fun deleteNotification(Id: Long): ResponseEntity<Void> {
        val notification = notificationAdminRepository.deleteById(Id)
        return ResponseEntity.ok().build()
    }
}