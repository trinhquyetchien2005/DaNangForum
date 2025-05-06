package com.example.DaNangForum.controller

import com.example.DaNangForum.repository.UserRepository
import com.example.DaNangForum.service.post.PostService
import com.example.danangforum.model.Post
import com.example.danangforum.model.User
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/post")
class PostController(
    private val postService: PostService,
    private val userRepository: UserRepository,
    repository: UserRepository
){
    @GetMapping("/all")
    fun allPosts(): ResponseEntity<List<Post>> {
    val response =  postService.getAllPosts()
        return ResponseEntity.ok(response)
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    fun post(@PathVariable id: Long): ResponseEntity<Any> {
       val post = postService.getPostById(id)
        return ResponseEntity.ok(post)
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{user_id}/all")
    fun allPostsByUserId(@PathVariable user_id: Long): ResponseEntity<Any> {
        val user: User = userRepository.findById(user_id).orElse(null) ?: return ResponseEntity.notFound().build()
        val posts = postService.getPostsByUser(user)
        return ResponseEntity.ok(posts)
    }
}