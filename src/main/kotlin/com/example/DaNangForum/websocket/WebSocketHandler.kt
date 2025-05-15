package com.example.DaNangForum.websocket

import com.example.DaNangForum.dto.chat.MessageDTO
import com.example.DaNangForum.repository.MessageRepository
import com.example.DaNangForum.repository.UserRepository
import com.example.danangforum.model.Message
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.springframework.web.socket.*
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.net.URI
import java.time.LocalDateTime

@Component
class ChatWebSocketHandler(
    private val objectMapper: ObjectMapper,
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository
) : TextWebSocketHandler() {

    private val sessions = mutableMapOf<Long, WebSocketSession>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val userId = session.uri?.getQueryParameter("userId")?.toLongOrNull()
        if (userId != null) {
            sessions[userId] = session
            session.attributes["userId"] = userId
            println("✅ User $userId connected to WebSocket.")

//            // 👉 Truy vấn và log tin nhắn cũ
//            val receiver = userRepository.findById(userId)
//            if (receiver.isPresent) {
//                val messages = messageRepository.findTop100ByReceiverOrderByCreateAtDesc(receiver.get())
//                if (messages.isNotEmpty()) {
//                    println("📨 Tin nhắn đến user $userId:")
//                    messages.forEach { msg ->
//                        println("  • Từ ${msg.sender.username}: ${msg.content} (${msg.createAt})")
//                    }
//                } else {
//                    println("ℹ️ Không có tin nhắn nào đến user $userId.")
//                }
//            } else {
//                println("❌ Không tìm thấy user trong DB để lấy tin nhắn.")
//            }

        } else {
            println("❌ Missing or invalid userId in WebSocket connection URL.")
            session.close(CloseStatus.BAD_DATA)
        }
    }


    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        try {
            val payload = message.payload
            val chatMessage = objectMapper.readValue(payload, MessageDTO::class.java)

            val senderId = session.attributes["userId"] as? Long
                ?: throw IllegalArgumentException("Sender ID not found in session.")

            val sender = userRepository.findById(senderId)
                .orElseThrow { IllegalArgumentException("Sender not found in database.") }

            val receiverId = chatMessage.receiverId
            val receiver = receiverId?.toLong()?.let { userRepository.findById(it) }
                ?.orElseThrow { IllegalArgumentException("Receiver not found in database.") }

            val newMessage = Message(
                content = chatMessage.content,
                sender = sender,
                receiver = receiver,
                createAt = LocalDateTime.now(),
                image = chatMessage.image,
                video = chatMessage.video
            )

            messageRepository.save(newMessage)

            val jsonMessage = objectMapper.writeValueAsString(chatMessage)
            sessions[receiverId]?.sendMessage(TextMessage(jsonMessage))

            println("📨 Message sent from user $senderId to $receiverId")
        } catch (e: Exception) {
            println("❗ Error handling message: ${e.message}")
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val userId = session.attributes["userId"] as? Long
        if (userId != null) {
            sessions.remove(userId)
            println("🔌 User $userId disconnected from WebSocket.")
        }
    }

    // Helper to parse query parameters from URI
    private fun URI.getQueryParameter(name: String): String? {
        return this.query?.split("&")
            ?.mapNotNull {
                val parts = it.split("=")
                if (parts.size == 2) parts[0] to parts[1] else null
            }?.toMap()?.get(name)
    }
}
