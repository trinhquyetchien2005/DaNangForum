package com.example.DaNangForum.repository.admin

import com.example.danangforum.model.Post
import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface PostAdminRepository: JpaRepository<Post, Long>{
        @Query("""
       SELECT COUNT(p)
       FROM Post p
       WHERE FUNCTION('DATE', p.createAt) = :date
    """)
        fun countByDate(@Param("date") date: LocalDate): Long

        fun findTop10ByOrderByCreateAtDesc(): List<Post>
}