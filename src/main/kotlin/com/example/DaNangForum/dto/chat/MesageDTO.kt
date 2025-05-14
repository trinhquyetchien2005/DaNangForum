package com.example.DaNangForum.dto.chat

data class MessageDTO(
    val senderId: Long,  // ID người gửi
    val receiverId: Long?,  // ID người nhận (có thể null)
    val content: String?,  // Nội dung tin nhắn
    val image: String?,  // Đường dẫn đến ảnh đính kèm (có thể null)
    val video: String?  // Đường dẫn đến video đính kèm (có thể null)
)
