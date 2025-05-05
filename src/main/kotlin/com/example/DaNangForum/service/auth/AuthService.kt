package com.example.DaNangForum.service.auth

import com.example.DaNangForum.dto.auth.ApiResponse
import com.example.DaNangForum.dto.auth.AuthResponse
import com.example.DaNangForum.dto.auth.LoginRequest
import com.example.DaNangForum.dto.auth.RegisterRequest
import com.example.DaNangForum.dto.user.UserDto
import com.example.DaNangForum.repository.UserRepository
import com.example.DaNangForum.security.JwtUtils
import com.example.danangforum.model.AuthProvider
import com.example.danangforum.model.User
import com.nimbusds.jose.shaded.gson.JsonObject
import com.nimbusds.jose.shaded.gson.JsonParser
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtils: JwtUtils,
    private val redisTemplate: RedisTemplate<String, String>,
) {

    fun register(request: RegisterRequest): AuthResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email đã tồn tại")
        }

        val newUser = User(
            username = request.username,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            role = "USER",
            school = "",
            avatar = "",
            phoneNumber = "",
            bio = "",
            address = "",
            create_at = LocalDateTime.now(),
            provider = AuthProvider.LOCAL
        )

        userRepository.save(newUser)

        val accessToken = jwtUtils.generateAccessToken(newUser.email)
        val refreshToken = jwtUtils.generateRefreshToken(newUser.email)

        redisTemplate.opsForValue().set("refresh:${newUser.email}", refreshToken)

        return AuthResponse(accessToken, refreshToken)
    }

    fun login(request: LoginRequest): AuthResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw UsernameNotFoundException("Tài khoản không tồn tại")

        // Kiểm tra tài khoản có phải tài khoản đăng nhập Google không
        if (user.provider != AuthProvider.LOCAL) {
            throw BadCredentialsException("Tài khoản này chỉ hỗ trợ đăng nhập bằng Google")
        }

        // Kiểm tra mật khẩu
        if (!passwordEncoder.matches(request.password, user.password)) {
            throw BadCredentialsException("Sai email hoặc mật khẩu")
        }

        // Tạo access token và refresh token
        val accessToken = jwtUtils.generateAccessToken(user.email)
        val refreshToken = jwtUtils.generateRefreshToken(user.email)

        // Lưu vào Redis
        redisTemplate.opsForValue().set("refresh:${user.email}", refreshToken, Duration.ofDays(7))

        // Trả về response
        return AuthResponse(accessToken, refreshToken)
    }

    fun loginWithGoogle(token: String): AuthResponse {
        try {
            // Xác minh token Google và lấy thông tin người dùng
            val googleUserInfo = verifyGoogleToken(token)

            val email = googleUserInfo.get("email").asString

            // Kiểm tra xem người dùng đã tồn tại trong DB chưa
            val existingUser = userRepository.findByEmail(email)

            val jwtToken: String
            if (existingUser != null) {
                // Kiểm tra provider
                if (existingUser.provider != AuthProvider.GOOGLE) {
                    // Nếu người dùng đã tồn tại nhưng dùng provider khác, ném lỗi
                    throw IllegalArgumentException("This account was registered with ${existingUser.provider}. Please log in with the appropriate provider.")
                }
                // Nếu người dùng đã tồn tại và sử dụng Google, tạo JWT mới
                jwtToken = jwtUtils.generateAccessToken(existingUser.email)
            } else {
                // Nếu chưa tồn tại, tạo mới người dùng và trả về JWT
                val newUser = User(
                    username = googleUserInfo.get("name").asString,
                    email = email,
                    role = "USER", // Có thể tùy chỉnh role ở đây
                    school = "",
                    avatar = googleUserInfo.get("picture").asString,
                    phoneNumber = "",
                    bio = "",
                    address = "",
                    create_at = LocalDateTime.now(),
                    provider = AuthProvider.GOOGLE // Đảm bảo provider là GOOGLE
                )
                userRepository.save(newUser)
                jwtToken = jwtUtils.generateAccessToken(newUser.email)
            }

            // Tạo refresh token và lưu vào Redis
            val refreshToken = jwtUtils.generateRefreshToken(email)

            // Lưu token vào Redis
            redisTemplate.opsForValue().set("refresh:$email", refreshToken, Duration.ofDays(7))

            // Trả về response với Google ID Token và JWT của hệ thống
            return AuthResponse(jwtToken, refreshToken)

        } catch (e: Exception) {
            // Tạo OAuth2Error và ném OAuth2AuthenticationException nếu xác minh token thất bại
            val oauth2Error = OAuth2Error("invalid_token", "Google authentication failed", null)
            throw OAuth2AuthenticationException(oauth2Error, e)
        }
    }

    private fun verifyGoogleToken(token: String): JsonObject {
        // Gọi API Google để xác minh token và lấy thông tin người dùng
        val url = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=$token"


        // Tạo client OkHttp
        val client = OkHttpClient()

        // Tạo yêu cầu GET
        val request = Request.Builder()
            .url(url)
            .build()

        // Gửi yêu cầu và nhận phản hồi
        val response = client.newCall(request).execute()

        if (!response.isSuccessful) {
            // Tạo OAuth2Error và ném OAuth2AuthenticationException nếu token không hợp lệ
            val oauth2Error = OAuth2Error("invalid_token", "Invalid Google token", null)
            throw OAuth2AuthenticationException(oauth2Error)
        }

        // Phân tích JSON trả về từ Google API
        val jsonResponse = response.body?.string()
        return JsonParser.parseString(jsonResponse).asJsonObject
    }

    fun refreshAccessToken(refreshToken: String): AuthResponse {
        val email = jwtUtils.getEmailFromToken(refreshToken)
        val key = "refresh:$email"
        val storedRefreshToken = redisTemplate.opsForValue().get(key)

        if (storedRefreshToken == null || storedRefreshToken != refreshToken) {
            throw IllegalArgumentException("Refresh token không hợp lệ hoặc đã hết hạn")
        }

        // Tạo mới access token và refresh token
        val newAccessToken = jwtUtils.generateAccessToken(email)
        val newRefreshToken = jwtUtils.generateRefreshToken(email)

        // Cập nhật Redis
        redisTemplate.opsForValue().set(key, newRefreshToken)
        redisTemplate.expire(key, Duration.ofDays(7))

        return AuthResponse(newAccessToken, newRefreshToken)
    }

    fun changePassword(email: String, oldPassword: String, newPassword: String): ApiResponse {
        val user = userRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("User not found with email: $email")

        // Kiểm tra tài khoản đăng nhập qua Google không cho phép đổi mật khẩu
        if (user.provider == AuthProvider.GOOGLE) {
            throw BadCredentialsException("Google account detected. Please log in with the appropriate provider.")
        }

        // Kiểm tra mật khẩu cũ và mật khẩu mới không trùng nhau
        if (oldPassword == newPassword) {
            throw BadCredentialsException("New password cannot be the same as the old password")
        }

        // Kiểm tra mật khẩu cũ có đúng không
        if (!passwordEncoder.matches(oldPassword, user.password)) {
            throw BadCredentialsException("Old password does not match the current password")
        }

        // Mã hóa mật khẩu mới trước khi lưu
        user.password = passwordEncoder.encode(newPassword)
        userRepository.save(user)

        return ApiResponse("Password changed successfully",null)
    }

    fun getUserInfoFromAccessToken(accessToken: String): UserDto {
        val email = jwtUtils.getEmailFromToken(accessToken)
            ?: throw IllegalArgumentException("Access token không hợp lệ")

        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("Không tìm thấy người dùng")

        return UserDto(user.user_id!!, user.email, user.username, user.role)
    }

}
