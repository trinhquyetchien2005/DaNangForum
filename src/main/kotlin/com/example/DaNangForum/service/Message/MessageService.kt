package com.example.DaNangForum.service

import com.example.DaNangForum.dto.chat.MessageDTO
import com.example.DaNangForum.repository.MessageRepository
import com.example.DaNangForum.repository.UserRepository
import com.example.danangforum.model.Message
import com.example.danangforum.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MessageService @Autowired constructor(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository  // Để lấy thông tin người dùng
) {

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
