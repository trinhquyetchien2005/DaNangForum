package com.example.DaNangForum.controller

import com.example.DaNangForum.dto.ApiResponse
import com.example.DaNangForum.service.admin.PostAdminService
import com.example.danangforum.model.Post
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/admin/post")
class PostAdminController (
    private val postAdminService: PostAdminService
){
    @GetMapping("/allPosts")
fun getAllPosts(): ResponseEntity<List<Post>> {
    return postAdminService.getAllPost()
}
    @GetMapping("/postId")
    fun getPostById(postId: Long): ResponseEntity<Post> {
        return postAdminService.getPost(postId)
    }

    @DeleteMapping("/postId")
    fun deletePostById(postId: Long): ResponseEntity<ApiResponse> {
        return postAdminService.deletePost(postId)
    }
}