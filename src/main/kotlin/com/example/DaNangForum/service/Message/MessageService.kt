package com.example.DaNangForum.service

import com.example.DaNangForum.dto.chat.MessageDTO
import com.example.DaNangForum.repository.MessageRepository
import com.example.DaNangForum.repository.UserRepository
import com.example.danangforum.model.Message
import com.example.danangforum.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Service

@Service
class MessageService @Autowired constructor(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository  // Để lấy thông tin người dùng
) {

    fun getMessages(receiverId: Long): ResponseEntity<List<Message>> {
        val receiver = userRepository.findById(receiverId).orElse(null) ?: return ResponseEntity.notFound().build()

        val auth = SecurityContextHolder.getContext().authentication
        val emailfromtoken = auth.name
        val userFromEmail = userRepository.findByEmail(emailfromtoken)

        if ( userFromEmail == null ) {
            return ResponseEntity.status(400).body(null)
        }

        val listMessages = messageRepository.findMessagesBySenderAndReceiverOrderByCreateAtDesc(userFromEmail, receiver)
        if ( listMessages.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
        }

        return ResponseEntity.status(200).body(listMessages)
    }

    fun saveMessage(messageDTO: MessageDTO): Message {
        // Lấy người gửi từ DB theo senderId
        val sender: User = userRepository.findById(messageDTO.senderId)
            .orElseThrow { RuntimeException("User not found with id: ${messageDTO.senderId}") }

        // Lấy người nhận từ DB (nếu có) theo receiverId
        val receiver: User? = messageDTO.receiverId?.let {
            userRepository.findById(it).orElseThrow { RuntimeException("User not found with id: $it") }
        }

        // Tạo đối tượng Message
        val message = Message(
            sender = sender,
            receiver = receiver,
            content = messageDTO.content,
            image = messageDTO.image,
            video = messageDTO.video
        )

        // Lưu tin nhắn vào DB
        return messageRepository.save(message)
    }

    // Lấy 100 tin nhắn gần nhất của người nhận
    fun getRecentMessages(receiverId: Long): List<Message> {
        val userChat = userRepository.findById(receiverId).orElseThrow { RuntimeException("User not found with id: $receiverId") }
        return messageRepository.findTop100ByReceiverOrderByCreateAtDesc(userChat)
    }
}
