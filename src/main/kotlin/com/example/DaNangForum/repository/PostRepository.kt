package com.example.DaNangForum.repository

import com.example.danangforum.model.Group
import com.example.danangforum.model.Post
import com.example.danangforum.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository: JpaRepository<Post, Long> {
    // Tìm tất cả bài viết
    override fun findAll(): List<Post>

    // Thay đổi tên phương thức để khớp với thuộc tính trong entity
    fun findByPostId(postId: Long): Post?  // Chú ý tên phương thức phải là findBy<property_name>

    fun findByUser(user: User): List<Post>

    fun deletePostByPostId(postId: Long)

    fun findByGroup(group: Group): List<Post>
}
