package com.example.DaNangForum.repository

import com.example.danangforum.model.Message
import com.example.danangforum.model.User
import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : JpaRepository<Message, Long> {
    @Query("""
    SELECT m FROM Message m 
    WHERE 
        (m.sender = :user1 AND m.receiver = :user2) 
        OR 
        (m.sender = :user2 AND m.receiver = :user1)
    ORDER BY m.createAt ASC
""")
    fun findConversationBetweenUsers(
        @Param("user1") user1: User,
        @Param("user2") user2: User
    ): List<Message>


    fun findMessagesBySenderAndReceiverOrderByCreateAtDesc(sender: User, receiver: User): List<Message>
}
