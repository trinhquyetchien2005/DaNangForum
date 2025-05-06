package com.example.DaNangForum.dto.auth

import java.time.LocalDate

data class RegisterRequest(
    val username: String,
    val email: String,
    val address: String,
    val dateOfBirth: LocalDate,
    val phone : String,
    val password: String,
    val school: String,
    )