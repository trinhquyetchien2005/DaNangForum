package com.example.DaNangForum.controller

import com.example.DaNangForum.service.MessageService
import com.example.danangforum.model.Message
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/message")
class GetMessageController(
    private val messageService: MessageService,
) {
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{receiverId}")
    fun getMessage( @RequestParam receiverId: Long): ResponseEntity<List<Message>> {
        return messageService.getMessages(receiverId)
    }
}