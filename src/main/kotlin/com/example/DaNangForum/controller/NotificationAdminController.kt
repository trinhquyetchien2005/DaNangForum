package com.example.DaNangForum.controller

import com.example.DaNangForum.service.admin.NotificationAdminService
import com.example.danangforum.model.Notification
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/admin/notification")
class NotificationAdminController (
    private val adminService: NotificationAdminService,
){
    @PostMapping("/create")
    fun create(@RequestBody notification: Notification): ResponseEntity<Notification> {
        return adminService.create(notification)
    }

    @GetMapping("/list")
    fun getNotifications(): ResponseEntity<List<Notification>> {
        return adminService.findAll()
    }

    @DeleteMapping("/notificationId")
    fun deleteNotification(notificationId: Long): ResponseEntity<Void> {
        return adminService.deleteNotification(notificationId)
    }

}