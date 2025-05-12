//package com.example.DaNangForum.websocket
//
//object WebSocketManager {
//    private var stompClient: StompClient? = null
//
//    fun connect(userId: String, onMessageReceived: (String) -> Unit) {
//        stompClient = Stomp.over(
//            Stomp.ConnectionProvider.OKHTTP,
//            "ws://10.0.2.2:8080/ws-chat/websocket" // localhost cho Android Emulator
//        )
//
//        stompClient?.connect()
//
//        // Subscribe nhận tin nhắn cá nhân
//        stompClient?.topic("/user/$userId/queue/messages")?.subscribe { event ->
//            onMessageReceived(event.payload)
//        }
//
//        // Ví dụ: Sub nhóm (groupId là "group1")
//        stompClient?.topic("/topic/group/group1")?.subscribe { event ->
//            onMessageReceived(event.payload)
//        }
//    }
//
//    fun sendMessage(message: String) {
//        stompClient?.send("/app/chat.send", message)?.subscribe()
//    }
//
//    fun disconnect() {
//        stompClient?.disconnect()
//    }
//}
