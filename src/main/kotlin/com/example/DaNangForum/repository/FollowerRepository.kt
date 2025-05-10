package com.example.DaNangForum.repository

import com.example.danangforum.model.Follower
import com.example.danangforum.model.User
import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FollowerRepository : JpaRepository<Follower, Long> {

    // Danh sách người mình đang theo dõi
    @Query("SELECT f.following FROM Follower f WHERE f.follower = :user")
    fun findFollowingByUser(@Param("user") user: User): List<User>

    // Danh sách người đang theo dõi mình
    @Query("SELECT f.follower FROM Follower f WHERE f.following = :user")
    fun findFollowersByUser(@Param("user") user: User): List<User>

    // Kiểm tra có phải bạn bè
    @Query("""
        SELECT CASE WHEN COUNT(f1) > 0 AND COUNT(f2) > 0 THEN true ELSE false END
        FROM Follower f1, Follower f2
        WHERE f1.follower = :user1 AND f1.following = :user2
          AND f2.follower = :user2 AND f2.following = :user1
    """)
    fun isFriend(@Param("user1") user1: User, @Param("user2") user2: User): Boolean

    // Danh sách bạn bè
    @Query("""
        SELECT f1.following FROM Follower f1
        JOIN Follower f2 ON f2.follower = f1.following AND f2.following = f1.follower
        WHERE f1.follower = :user
    """)
    fun findFriends(@Param("user") user: User?): List<User>

    @Query("""
    SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END
    FROM Follower f
    WHERE f.follower = :follower AND f.following = :following
""")
    fun isFollowing(
        @Param("follower") follower: User,
        @Param("following") following: User
    ): Boolean


}
