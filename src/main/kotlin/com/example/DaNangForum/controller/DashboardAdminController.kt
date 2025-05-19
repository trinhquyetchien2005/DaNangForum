package com.example.DaNangForum.controller

import com.example.DaNangForum.dto.admin.AdminDTO
import com.example.DaNangForum.repository.UserRepository
import com.example.DaNangForum.service.admin.DashboardAdminService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/admin/dashboard"])
class DashboardAdminController (
    private val dashboardAdminService: DashboardAdminService,
    private val userRepository: UserRepository,
){
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/user/count")
    fun countUser(): ResponseEntity<Long> {
        return dashboardAdminService.countUsers()
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/post/count")
    fun countPost(): ResponseEntity<Long> {
        return dashboardAdminService.countPosts()
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/post/today")
    fun countPostToday(): ResponseEntity<Long> {
        return dashboardAdminService.countPostsCreatedToday()
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/user/today")
    fun countUserToday(): ResponseEntity<Long> {
        return dashboardAdminService.countUsersCreatedToday()
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/info")
    fun getAdminInfo(): ResponseEntity<AdminDTO> {
        val auth = SecurityContextHolder.getContext().authentication
        val email = auth.name

        return ResponseEntity.status(200).body(AdminDTO(email))
    }

}