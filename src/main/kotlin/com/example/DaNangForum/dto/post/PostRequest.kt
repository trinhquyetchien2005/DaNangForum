package com.example.DaNangForum.dto.post

data class PostRequest (
    val content: String,
    val imageUrl: String? = null,
    val videoUrl: String? = null,
)