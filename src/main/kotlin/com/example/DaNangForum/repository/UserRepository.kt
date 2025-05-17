package com.example.DaNangForum.repository

import com.example.DaNangForum.dto.user.UserStatsDto
import com.example.danangforum.model.User
import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

    /* ---------- CRUD / Search có sẵn ---------- */
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
    fun deleteByEmail(email: String)

    @Query(
        "SELECT u FROM User u " +
                "WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))"
    )
    fun searchByUsernameLike(@Param("username") username: String): List<User>




    @Query(
        value = """
            SELECT
                (SELECT COUNT(*) FROM follower f  WHERE f.following_id = :userId)                           AS followers,
                (SELECT COUNT(*) FROM follower f  WHERE f.follower_id  = :userId)                           AS following,
                (SELECT COUNT(*) FROM `like`  l
                       JOIN post p ON p.post_id = l.post_id
                     WHERE p.user_id = :userId)                                                             AS likes
        """,
        nativeQuery = true
    )
    fun fetchUserStats(@Param("userId") userId: Long): UserStatsDto
}
