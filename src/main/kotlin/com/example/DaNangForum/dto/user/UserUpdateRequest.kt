package com.example.DaNangForum.dto.user

import java.time.LocalDate

data class UserUpdateRequest (
    val name: String,
    val address: String,
    val dateOfBirth: LocalDate,
    val bio: String,
    val school: String,
    val avatar: String,
    val phoneNumber: String,
)

