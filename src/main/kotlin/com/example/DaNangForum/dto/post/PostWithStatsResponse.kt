package com.example.DaNangForum.dto.post

import com.example.danangforum.model.Post

data class PostWithStatsResponse(
    val post: Post,
    val likeCount: Int,
    val commentCount: Int
)
