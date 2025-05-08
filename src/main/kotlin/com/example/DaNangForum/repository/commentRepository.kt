package com.example.DaNangForum.repository

import com.example.danangforum.model.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface commentRepository: JpaRepository<Comment, Long> {
    fun findCommentsByPost_PostId(postPostId: Long): MutableList<Comment>
}