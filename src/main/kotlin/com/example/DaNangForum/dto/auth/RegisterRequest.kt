package com.example.DaNangForum.dto.auth

data class RegisterRequest(
    val email: String,
    val password: String,
    val username: String
)