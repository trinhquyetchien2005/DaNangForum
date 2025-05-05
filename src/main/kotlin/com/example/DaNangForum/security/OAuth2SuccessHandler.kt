package com.example.DaNangForum.security

import com.example.DaNangForum.repository.UserRepository
import com.example.danangforum.model.AuthProvider
import com.example.danangforum.model.User
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class OAuth2SuccessHandler(
    private val userRepository: UserRepository,
    private val jwtUtils: JwtUtils,
    private val redisTemplate: RedisTemplate<String, String>
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oauthUser = authentication.principal as DefaultOAuth2User
        val email = oauthUser.getAttribute<String>("email") ?: throw RuntimeException("Email không có!")

        val user = userRepository.findByEmail(email) ?: userRepository.save(
            User(
                username = email.substringBefore("@"),
                email = email,
                role = "USER",
                password = null,
                school = "",
                avatar = oauthUser.getAttribute("picture") ?: "",
                phoneNumber = "",
                bio = "",
                address = "",
                create_at = LocalDateTime.now(),
                provider = AuthProvider.GOOGLE
            )
        )

        val jwt = jwtUtils.generateAccessToken(user.email)

        // lưu vào Redis (email làm key)
        redisTemplate.opsForValue().set(user.email, jwt)

        // trả về JWT dưới dạng JSON
        response.contentType = "application/json"
        response.writer.write("""{"token": "$jwt"}""")
    }
}
