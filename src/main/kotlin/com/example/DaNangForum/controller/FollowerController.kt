package com.example.DaNangForum.controller

import com.example.DaNangForum.dto.ApiResponse
import com.example.DaNangForum.service.FollowerAndBlock.FollowerService
import com.example.danangforum.model.User
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/follower")
class FollowerController(val followerService: FollowerService) {

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/friend")
    fun getFriend(): ResponseEntity<List<User>> {
        return followerService.getFriends()
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/GetFollower")
    fun getFollower(): ResponseEntity<List<User>> {
        return followerService.getFollowers()
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/GetFollowing")
    fun getFollowing(): ResponseEntity<List<User>> {
        return followerService.getFollowing()
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/Follow")
    fun Follow(@RequestBody userId: Long): ResponseEntity<ApiResponse> {
        return followerService.Follow(userId)
    }
}