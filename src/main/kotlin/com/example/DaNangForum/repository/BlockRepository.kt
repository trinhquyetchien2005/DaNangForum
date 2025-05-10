package com.example.DaNangForum.repository

import com.example.danangforum.model.Block
import com.example.danangforum.model.User
import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BlockRepository: JpaRepository<Block, User>{
    @Query("SELECT b.blocked FROM Block b WHERE b.blocker = :blocker")
    fun findBlockedUsersByBlocker(@Param("blocker") blocker: User): List<User>

    @Query("""
    SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
    FROM Block b
    WHERE b.blocker = :blocker AND b.blocked = :blocked
""")
    fun isBlocked(
        @Param("blocker") blocker: User,
        @Param("blocked") blocked: User
    ): Boolean
}
