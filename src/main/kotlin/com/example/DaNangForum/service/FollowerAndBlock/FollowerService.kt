package com.example.DaNangForum.service.FollowerAndBlock

import com.example.DaNangForum.dto.ApiResponse
import com.example.DaNangForum.repository.BlockRepository
import com.example.DaNangForum.repository.FollowerRepository
import com.example.DaNangForum.repository.UserRepository
import com.example.danangforum.model.Block
import com.example.danangforum.model.Follower
import com.example.danangforum.model.User
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class FollowerService(
    val followerRepository: FollowerRepository,
    val userRepository: UserRepository,
    val blockRepository: BlockRepository
) {
    fun getFriends(): ResponseEntity<List<User>> {
        val auth = SecurityContextHolder.getContext().authentication
        val email = auth.name
        val user = userRepository.findByEmail(email)

        val listUser =  followerRepository.findFriends(user)
        if (listUser == null){
            return ResponseEntity.status(404).body(null)
        }
        return ResponseEntity.ok().body(listUser)
    }

    fun getFollowers(): ResponseEntity<List<User>> {
        val auth = SecurityContextHolder.getContext().authentication
        val email = auth.name
        val user = userRepository.findByEmail(email)

        if (user == null){
            return ResponseEntity.status(404).body(null)
        }
        val listUser = followerRepository.findFollowersByUser(user)
        return ResponseEntity.ok().body(listUser)
    }

    fun getFollowing(): ResponseEntity<List<User>> {
        val auth = SecurityContextHolder.getContext().authentication
        val email = auth.name
        val user = userRepository.findByEmail(email)

        if (user == null){
            return ResponseEntity.status(404).body(null)
        }
        val listUser = followerRepository.findFollowingByUser(user)
        return ResponseEntity.ok().body(listUser)
    }

    fun Follow(userId: Long): ResponseEntity<ApiResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        val email = auth.name
        val user = userRepository.findByEmail(email)

        if (user == null){
            return ResponseEntity.status(401).body(null)
        }

        val followUser = userRepository.findById(userId).orElse(null)?:return ResponseEntity.status(404).body(null)

        val exitsBlockUser = blockRepository.isBlocked(user, followUser)
        if (exitsBlockUser){
            return ResponseEntity.status(400).body(ApiResponse("ban dang block nguoi nay", followUser))
        }

        val follow = Follower(
            follower = user,
            following = followUser
        )

        followerRepository.save(follow)

        return ResponseEntity.ok().body(ApiResponse("Following", follow))
    }

    fun getPeople(): ResponseEntity<List<User>> {
        val auth = SecurityContextHolder.getContext().authentication
        val email = auth.name
        val user = userRepository.findByEmail(email)

        if (user == null){
            return ResponseEntity.status(404).body(null)
        }

        val listUser = followerRepository.findUsersNotFollowedAndNotBlocked(user)

        return ResponseEntity.ok().body(listUser)
    }

    fun unFollow(userId: Long): ResponseEntity<ApiResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        val email = auth.name
        val user = userRepository.findByEmail(email)?: return ResponseEntity.status(404).body(null)

        val followingUser = userRepository.findById(userId).orElse(null)?:return ResponseEntity.status(404).body(null)

        val unfollow = followerRepository.deleteByFollowerAndFollowing(user, followingUser)

        return ResponseEntity.ok().body(ApiResponse("Unfollowing", unfollow))
    }
}