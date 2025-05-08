package com.example.DaNangForum.dto.auth

import com.example.DaNangForum.dto.user.UserDto

data class AuthResponse(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val user: UserDto? = null
)

