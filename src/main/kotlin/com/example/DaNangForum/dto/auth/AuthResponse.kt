package com.example.DaNangForum.dto.auth

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String? = null  // Trả về Google ID Token nếu có
)

