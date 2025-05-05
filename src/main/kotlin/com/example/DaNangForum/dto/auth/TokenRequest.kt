package com.example.DaNangForum.dto.auth

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class TokenRequest @JsonCreator constructor(
    @JsonProperty("token") val token: String
)
