package com.example.DaNangForum.service.post


import com.example.DaNangForum.dto.ApiResponse
import com.example.DaNangForum.dto.post.PostGetRequest
import com.example.DaNangForum.dto.post.PostRequest
import com.example.DaNangForum.dto.post.PostUpdateRequest
import com.example.DaNangForum.dto.post.PostWithStatsResponse
import com.example.DaNangForum.dto.user.UserDto
import com.example.DaNangForum.repository.LikeRepository
import com.example.DaNangForum.repository.PostRepository
import com.example.DaNangForum.repository.UserRepository
import com.example.DaNangForum.repository.commentRepository
import com.example.danangforum.model.Comment
import com.example.danangforum.model.Like
import com.example.danangforum.model.Post
import com.example.danangforum.model.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val likeRepository: LikeRepository,
    private val commentRepository: commentRepository
) {

    fun getPostById(id: Long): ResponseEntity<Post> {
        val post = postRepository.findByPostId(id)

        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
        return ResponseEntity.status(200).body(post)
    }

    fun getPostsByUser(user: User): ResponseEntity<List<Post>> {
        return ResponseEntity.status(200).body(postRepository.findByUser(user))
    }

    fun deletePostById(id: Long): ApiResponse {
        postRepository.deleteById(id)
        return ApiResponse("Post deleted", null)
    }
    fun createPost(postRequest: PostRequest): ResponseEntity<Post> {
        val auth = SecurityContextHolder.getContext().authentication
        val userDetails = auth.name
        val user = userRepository.findByEmail(userDetails)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        val newPost = Post(
            user = user,
            content = postRequest.content,
            image = postRequest.imageUrl,
            video = postRequest.videoUrl,
        )

        val savedPost = postRepository.save(newPost)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost)
    }

    fun updatePostById(postId: Long, postRequest: PostUpdateRequest): ResponseEntity<Post> {
        val postOptional = postRepository.findByPostId(postId)
        if (postOptional == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
        val auth = SecurityContextHolder.getContext().authentication
        val userDetails = auth.name
        val user = userRepository.findByEmail(userDetails)

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }

        if(postOptional.user.email != user.email) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null)
        }

            postOptional.user = user
            postOptional.content = postRequest.content
            postOptional.image = postRequest.image
            postOptional.video = postRequest.video

        val savedPost = postRepository.save(postOptional)

        return ResponseEntity.status(HttpStatus.OK).body(savedPost)
    }

    fun likePostById(postId: Long): ResponseEntity<ApiResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        val emailfromtoken = auth.name
        val user = userRepository.findByEmail(emailfromtoken)?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)

        if(postRepository.findByPostId(postId) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse("Post not found", null))
        }

        val check = likeRepository.findByPost_PostIdAndUser_UserId(postId, user.userId)

        return if (check == null) {
            val post = postRepository.findByPostId(postId)?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
            val like = Like(
                post = post,
                user = user
            )
            likeRepository.save(like)
            ResponseEntity.status(201).body(ApiResponse("like",like))
        }else{
            likeRepository.delete(check)
            ResponseEntity.status(200).body(ApiResponse("Post unliked", null))
        }
    }

    fun getAllCommentsForPost(postId: Long): ResponseEntity<List<Comment>> {
        val allComment = commentRepository.findCommentsByPost_PostId(postId)
        val post = postRepository.findByPostId(postId)
        if(post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }

        if (allComment == null || allComment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
        }
        return ResponseEntity.status(HttpStatus.OK).body(allComment)
    }

    fun updateCommentById(commentId: Long, content: String): ResponseEntity<ApiResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        val emailfromtoken = auth.name
        val user = userRepository.findByEmail(emailfromtoken)?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)

        val comment = commentRepository.findById(commentId).orElse(null)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse("Comment not found", false))

        val commentOnwer = comment.user!!.userId
        if (commentOnwer != user.userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse("Comment not your", false))
        }
        comment.content = content

        commentRepository.save(comment)
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse("Comment", content))
    }

    fun removeCommentById(commentId: Long): ResponseEntity<ApiResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        val user = userRepository.findByEmail(auth.name) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        val comment = commentRepository.findById(commentId).orElse(null)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse("Comment not found", false))

        val commentOnwer = comment.user!!.userId
        val postOnwer = comment.post!!.user.userId
        if (commentOnwer != user.userId && postOnwer != user.userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse("Access denied", false))
        }

        commentRepository.delete(comment)

        return ResponseEntity.ok(ApiResponse("Comment deleted successfully", true))
    }


    fun commentPostById(postId: Long, content: String): ResponseEntity<ApiResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        val emailfromtoken = auth.name
        val userFromAccessToken = userRepository.findByEmail(emailfromtoken)?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)

        val postComment = postRepository.findByPostId(postId)
        if(postComment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }

        val comment = Comment(
            content = content,
            user = userFromAccessToken,
            post = postComment
        )

        commentRepository.save(comment)

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse("comment", comment))
    }

    fun getAllPostsWithStats(): ResponseEntity<List<PostWithStatsResponse>> {

        val posts = postRepository.findAll()

        val postStats = posts.map { post ->
            val likeCount = likeRepository.countByPost_PostId(post.postId)
            val commentCount = commentRepository.countByPost_PostId(post.postId)

            val user = post.user
                val userdto = UserDto(user.userId, user.username, user.email, user.avatar)
                val postdto = PostGetRequest(
                    userdto = userdto,
                    content = post.content,
                    image = post.image,
                    video = post.video,
                    create_at = post.createAt
                )
                PostWithStatsResponse(postdto, likeCount, commentCount)

        }

        return ResponseEntity.status(200).body(postStats)
    }

}
