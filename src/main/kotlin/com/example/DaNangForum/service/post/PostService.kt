package com.example.DaNangForum.service.post


import com.example.DaNangForum.dto.ApiResponse
import com.example.DaNangForum.repository.PostRepository
import com.example.danangforum.model.Post
import com.example.danangforum.model.User
import org.springframework.stereotype.Service

@Service
class PostService(
     private val postRepository: PostRepository
) {

    fun getAllPosts(): List<Post> {
        return postRepository.findAll()
    }

    fun getPostById(id: Long): Any? {
        val post = postRepository.findByPostId(id)

        if (post == null) {
            return ApiResponse("Post not found", null)
        }
        return post
    }

    fun getPostsByUser(user: User): List<Post> {
        return postRepository.findByUser(user)
    }

    fun deletePostById(id: Long): ApiResponse {
        postRepository.deleteById(id)
        return ApiResponse("Post deleted", null)
    }
}
