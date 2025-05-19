package com.example.DaNangForum.controller

import com.example.DaNangForum.dto.ApiResponse
import com.example.DaNangForum.dto.auth.AuthResponse
import com.example.DaNangForum.service.admin.AuthAdminService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/admin/auth"])
class AuthAdminController (
    private val authAdminService: AuthAdminService
){
    @PostMapping("/login")
    fun login(@RequestParam email: String, @RequestParam password: String): ResponseEntity<AuthResponse> {
        return authAdminService.login(email, password)
    }

    @PostMapping("/logout")
    fun logout(): ResponseEntity<ApiResponse> {
        return authAdminService.logout()
    }
}