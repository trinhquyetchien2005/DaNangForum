package com.example.DaNangForum.dto.post

import com.fasterxml.jackson.annotation.JsonProperty

data class PostWithStatsResponse(
    val post: PostGetRequest,
    val likeCount: Int,
    val commentCount: Int,
    @JsonProperty("isLiked") val isliked: Boolean
)
