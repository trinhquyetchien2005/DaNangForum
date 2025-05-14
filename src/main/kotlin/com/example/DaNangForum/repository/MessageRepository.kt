package com.example.DaNangForum.repository

import com.example.danangforum.model.Message
import com.example.danangforum.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : JpaRepository<Message, Long> {
    fun findTop100ByReceiverOrderByCreateAtDesc(receiver: User): List<Message>
}
