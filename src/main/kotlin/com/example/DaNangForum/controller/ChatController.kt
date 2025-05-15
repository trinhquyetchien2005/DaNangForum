package com.example.DaNangForum.controller

import com.example.DaNangForum.dto.chat.MessageDTO
import com.example.DaNangForum.service.MessageService
import com.example.danangforum.model.Message
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.stereotype.Controller

@Controller
class WebSocketChatController @Autowired constructor(
    private val messageService: MessageService
) {

    @MessageMapping("/chat.send")  // Chạy khi người dùng gửi tin nhắn
    @SendToUser("/queue/messages")  // Gửi tin nhắn tới người dùng cụ thể
    fun sendMessage(messageDTO: MessageDTO): MessageDTO {
        // Lưu tin nhắn vào cơ sở dữ liệu
        messageService.saveMessage(messageDTO)

        // Trả về tin nhắn đã gửi cho client
        return messageDTO
    }

}
