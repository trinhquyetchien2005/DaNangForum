package com.example.DaNangForum.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {
    @GetMapping("/")
    fun home(): MutableMap<String, String> {
        val response: MutableMap<String, String> = HashMap()
        response["message"] = "Xin chào từ API JSON!"
        return response
    }
}