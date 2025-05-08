package com.example.DaNangForum.controller

import com.example.DaNangForum.dto.ApiResponse
import com.example.DaNangForum.dto.post.PostRequest
import com.example.DaNangForum.dto.post.PostUpdateRequest
import com.example.DaNangForum.repository.PostRepository
import com.example.DaNangForum.repository.UserRepository
import com.example.DaNangForum.security.JwtUtils
import com.example.DaNangForum.service.auth.AuthService
import com.example.DaNangForum.service.post.PostService
import com.example.danangforum.model.Comment
import com.example.danangforum.model.Like
import com.example.danangforum.model.Post
import com.example.danangforum.model.User
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/post")
class PostController(
    private val postService: PostService,
    private val userRepository: UserRepository,
    repository: UserRepository,
    val jwtUtils: JwtUtils,
    private val postRepository: PostRepository,
    private val authService: AuthService,
){
    @GetMapping("/all")
    fun allPosts(): ResponseEntity<List<Post>> {
    val response =  postService.getAllPosts()
        return ResponseEntity.ok(response)
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    fun post(@PathVariable id: Long): ResponseEntity<Post> {
        return postService.getPostById(id)
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/all/{user_id}")
    fun allPostsByUserId(@PathVariable user_id: Long): ResponseEntity<List<Post>> {
        val user: User = userRepository.findById(user_id).orElse(null) ?: return ResponseEntity.status(404).build()
        return  postService.getPostsByUser(user)
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/delete/{id}")
    fun deletePostById(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<ApiResponse> {
        val owner = jwtUtils.getEmailFromToken(jwtUtils.extractTokenFromRequest(request))
        val post = postRepository.findByPostId(id) ?: return ResponseEntity.notFound().build()

        if(post.user.email != owner){
            return ResponseEntity.notFound().build()
        }
        postService.deletePostById(id)
        return ResponseEntity.ok().build()
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/create")
    fun createPost(@RequestBody postRequest: PostRequest): ResponseEntity<Post> {
        return postService.createPost(postRequest)
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/update/{id}")
    fun updatePostById(@PathVariable id: Long, @RequestBody post: PostUpdateRequest): ResponseEntity<Post> {
        return postService.updatePostById(id, post)
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/like/{id}")
    fun likePostById(@PathVariable id: Long): ResponseEntity<ApiResponse> {
        return postService.likePostById(id)
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/comment/update/{id}")
    fun updateCommentById(@RequestParam id: Long, @RequestParam content: String): ResponseEntity<ApiResponse> {
        return postService.updateCommentById(id, content)
    }

    @GetMapping("/{postId}/comments")
    fun commentsByPostId(@PathVariable postId: Long): ResponseEntity<List<Comment>> {
        return postService.getAllCommentsForPost(postId)
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/comment/delete/{id}")
    fun deleteCommentById(@PathVariable id: Long): ResponseEntity<ApiResponse> {
        return postService.removeCommentById(id)
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/comment/create/{id}")
    fun commentPost(@PathVariable id: Long, content: String): ResponseEntity<ApiResponse> {
        return postService.commentPostById(id, content)
    }
}