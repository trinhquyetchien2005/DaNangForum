package com.example.DaNangForum.service.admin

import com.example.DaNangForum.dto.ApiResponse
import com.example.DaNangForum.dto.auth.AuthResponse
import com.example.DaNangForum.repository.admin.UserAdminRepository
import com.example.DaNangForum.security.JwtUtils
import com.example.DaNangForum.service.auth.RedisService
import com.example.danangforum.model.AuthProvider
import io.lettuce.core.KillArgs.Builder.user
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class AuthAdminService (
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtils: JwtUtils,
    private val redisService: RedisService,
    private val userAdminRepository: UserAdminRepository
){
    fun login(email: String, password: String): ResponseEntity<AuthResponse> {
        val user = userAdminRepository.findByEmail(email) ?: return ResponseEntity.status(404).body(null)

        if (user.role != "ADMIN") {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null)
        }

        if (user.provider != AuthProvider.LOCAL) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(AuthResponse(null, null))
        }

        if (!passwordEncoder.matches(password, user.password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AuthResponse(null, null))
        }

        val accessToken = jwtUtils.generateAccessToken(user.email)
        val refreshToken = jwtUtils.generateRefreshToken(user.email)
        redisService.saveToken(email, refreshToken)

        return ResponseEntity.status(HttpStatus.OK).body(AuthResponse(accessToken, refreshToken))

    }

    fun logout(): ResponseEntity<ApiResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        val email = auth.name

        redisService.deleteToken(email)
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse("Logout", null))
    }
}