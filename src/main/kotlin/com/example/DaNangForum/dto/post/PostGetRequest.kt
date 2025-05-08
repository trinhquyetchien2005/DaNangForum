package com.example.DaNangForum.dto.post

import com.example.DaNangForum.dto.user.UserDto
import java.time.LocalDateTime

data class PostGetRequest(
    val userdto: UserDto,
    val content: String,
    val image: String ?= null,
    val video: String ?= null,
    val create_at: LocalDateTime,
)