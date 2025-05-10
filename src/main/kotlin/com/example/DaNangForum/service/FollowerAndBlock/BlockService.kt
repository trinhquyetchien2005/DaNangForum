package com.example.DaNangForum.service.FollowerAndBlock

import com.example.DaNangForum.dto.ApiResponse
import com.example.DaNangForum.repository.BlockRepository
import com.example.DaNangForum.repository.FollowerRepository
import com.example.DaNangForum.repository.UserRepository
import com.example.danangforum.model.User
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class BlockService (
    val blockRepository: BlockRepository,
    val userRepository: UserRepository,
    val followerRepository: FollowerRepository,
){
    fun Block(userId: Long): ResponseEntity<ApiResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        val email = auth.name
        val user = userRepository.findByEmail(email)

        if (user == null){
            return ResponseEntity.status(401).body(null)
        }

        val blockUser = userRepository.findById(userId).orElse(null)

        val existsFolloweUser = followerRepository.isFollowing(user, blockUser)

        if (existsFolloweUser){
            return ResponseEntity.status(400).body(ApiResponse("ban dang follow nguoi nay", blockUser))
        }

        if (blockUser == null){
            return ResponseEntity.status(404).body(null)
        }

        val block = com.example.danangforum.model.Block(
            blocker = user,
            blocked = blockUser
        )

        blockRepository.save(block)
        return ResponseEntity.status(200).body(ApiResponse("Block successful", null))
    }

    fun getBlockedUser(): ResponseEntity<List<User>> {
        val auth = SecurityContextHolder.getContext().authentication
        val email = auth.name
        val user = userRepository.findByEmail(email)

        if (user == null){
            return ResponseEntity.status(401).body(null)
        }

        val listBlockedUser =  blockRepository.findBlockedUsersByBlocker(user)
        if (listBlockedUser == null){
            return ResponseEntity.status(204).body(null)
        }
        return ResponseEntity.status(200).body(listBlockedUser)
    }
}