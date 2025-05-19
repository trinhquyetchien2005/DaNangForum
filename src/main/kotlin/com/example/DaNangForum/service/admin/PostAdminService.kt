package com.example.DaNangForum.service.admin

import com.example.DaNangForum.dto.ApiResponse
import com.example.DaNangForum.repository.admin.PostAdminRepository
import com.example.danangforum.model.Post
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class PostAdminService(
    private val postAdminRepository: PostAdminRepository,
) {
    fun getAllPost(): ResponseEntity<List<Post>> {
        return ResponseEntity.ok(postAdminRepository.findAll())
    }
    fun getPost(postId: Long): ResponseEntity<Post> {
        return ResponseEntity.ok(postAdminRepository.findById(postId).orElse(null))
    }
    fun deletePost(postId: Long): ResponseEntity<ApiResponse> {
            return ResponseEntity.ok(ApiResponse("Delete post", postAdminRepository.deleteById(postId)))
    }
}