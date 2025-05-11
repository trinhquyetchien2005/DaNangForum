package com.example.DaNangForum.dto.Group

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class GroupDto @JsonCreator constructor(
    @JsonProperty("groupname")
    var groupname: String
)