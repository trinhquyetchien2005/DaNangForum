package com.example.DaNangForum.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(
    private val jwtUtils: JwtUtils,
    private val userDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {

    private val publicPathPatterns = listOf(
        "^/api/auth/register$",
        "^/api/auth/login$",
        "^/api/auth/google$",
        "^/api/auth/sendOtp$",
        "^/api/auth/verifyOtp$",
        "^/api/auth/refresh$",
        "^/api/auth/forgotPassword$",
        "^/api/auth/verifyOtpPassword$",
        "^/v3/api-docs.*",
        "^/swagger-ui.html$",
        "^/swagger-ui.*",
        "^/api/post/\\d+/comments$",
        "^/api/user/search/[^/]+$",
        "^/api/user/\\d+$",
        "^/ws/.*$",
        "^/api/message(/.*)?$"// ✅ Cho phép tất cả endpoint WebSocket như /ws/chat
    )


    private fun isPublicPath(path: String): Boolean {
        return publicPathPatterns.any { path.matches(Regex(it)) }
    }
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val path = request.servletPath

        // Nếu là endpoint public → bỏ qua luôn filter
        if (isPublicPath(path)) {
            filterChain.doFilter(request, response)
            return
        }

        // Các endpoint yêu cầu xác thực
        val header = request.getHeader("Authorization")
        val token = header?.takeIf { it.startsWith("Bearer ") }?.substring(7)

        if (token.isNullOrBlank()) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json"
            response.writer.write(
                """
                {
                    "status": 401,
                    "message": "Missing Authorization header"
                }
                """.trimIndent()
            )
            return
        }

        if (!jwtUtils.validateToken(token)) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json"
            response.writer.write(
                """
                {
                    "status": 401,
                    "message": "Invalid or expired token"
                }
                """.trimIndent()
            )
            return
        }

        // Token hợp lệ
        val email = jwtUtils.getEmailFromToken(token)
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(email)
        val auth = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        SecurityContextHolder.getContext().authentication = auth

        filterChain.doFilter(request, response)
    }
}
