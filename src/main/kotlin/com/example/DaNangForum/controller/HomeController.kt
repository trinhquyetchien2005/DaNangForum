package com.example.DaNangForum.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {
    @GetMapping("/")
    fun loginSuccess(@AuthenticationPrincipal user: OidcUser): String {
        // In ra email của người dùng sau khi đăng nhập thành công
        return "Đăng nhập thành công! Email: ${user.email}"
    }
}