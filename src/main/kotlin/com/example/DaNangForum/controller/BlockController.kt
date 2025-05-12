package com.example.DaNangForum.controller

import com.example.DaNangForum.dto.ApiResponse
import com.example.DaNangForum.repository.BlockRepository
import com.example.DaNangForum.service.FollowerAndBlock.BlockService
import com.example.danangforum.model.User
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/block")
class BlockController (
    val blockService: BlockService
){
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/Blocked")
    fun Block(@RequestBody userId: Long): ResponseEntity<ApiResponse> {
        return blockService.Block(userId)
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/getBlocked")
    fun getBlocked(): ResponseEntity<List<User>> {
        return blockService.getBlockedUser()
    }
}