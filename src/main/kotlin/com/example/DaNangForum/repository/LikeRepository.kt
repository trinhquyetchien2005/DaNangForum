package com.example.DaNangForum.repository

import com.example.danangforum.model.Like
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LikeRepository: JpaRepository<Like, Long> {
    fun findByPost_PostIdAndUser_UserId(postId: Long, userId: Long): Like?
    fun countByPost_PostId(postId: Long): Int

}