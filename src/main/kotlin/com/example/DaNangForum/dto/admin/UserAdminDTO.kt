package com.example.DaNangForum.dto.admin

import java.time.LocalDate

data class UserAdminDTO (
    val name: String,
    val email: String,
    val address: String,
    val dateOfBirth: LocalDate?,
    val bio: String,
    val school: String,
    val avatar: String,
    val phoneNumber: String,
    val role: String
)