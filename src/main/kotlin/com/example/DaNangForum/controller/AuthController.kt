package com.example.DaNangForum.controller

import com.example.DaNangForum.dto.ApiResponse
import com.example.DaNangForum.dto.auth.*
import com.example.DaNangForum.dto.user.UserDto
import com.example.DaNangForum.repository.UserRepository
import com.example.DaNangForum.security.JwtUtils
import com.example.DaNangForum.service.auth.AuthService
import com.example.DaNangForum.service.auth.RedisService
import com.example.DaNangForum.service.email.EmailService
import com.example.danangforum.model.AuthProvider
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.mail.Address
import jakarta.mail.MessagingException
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val jwtUtils: JwtUtils,
    private val emailService: EmailService,
    private val redisService: RedisService,
    private val userRepository: UserRepository,
) {
    // Đăng ký người dùng
    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest): ApiResponse {
        return try {
            sendOtp(registerRequest.email)
            ApiResponse("OTP sent to your email", null)
        } catch (ex: Exception) {
            ApiResponse(ex.message ?: "Registration failed", null)
        }
    }

    @PostMapping("/forgotPassword")
    fun forgotPassword(@RequestParam email: String): ApiResponse {
        val user = userRepository.findByEmail(email)
        ?: throw UsernameNotFoundException("User not found with email: $email")

        if (user.provider == AuthProvider.GOOGLE){
            return ApiResponse("Provider GOOGLE", null)
        }

        val otp = generateOtp() // Hàm tự tạo OTP
        val subject = "Your OTP refresh password"
        val body = "Your OTP refresh password is: $otp"

        emailService.sendEmail(email, subject, body)

        redisService.saveOtp(email, otp)

        return ApiResponse("Forgot Password sent to your email", otp)
    }

    @PatchMapping("/verifyOtpPassword")
    fun verifyOtpPassword(@RequestParam email: String, @RequestParam otp: String): ApiResponse {
        return authService.refreshPassword(email, otp)
    }

    // Gửi mã OTP
    @PostMapping("/sendOtp")
    @Throws(MessagingException::class)
    fun sendOtp(@RequestParam email: String) {
        val otp = generateOtp() // Hàm tự tạo OTP
        val subject = "Your OTP Code"
        val body = "Your OTP code is: $otp"

        emailService.sendEmail(email, subject, body)

        // Lưu OTP vào Redis (hoặc database)
        redisService.saveOtp(email, otp)

        println("OTP sent to $email: $otp")
    }

    // Tạo OTP ngẫu nhiên


    @PostMapping("/login")
    fun login(@RequestParam email: String, @RequestParam password: String): AuthResponse {
        return authService.login(LoginRequest(email, password))
    }

    @PostMapping("/refresh")
    fun refreshToken(@RequestParam email: String): Any {
        return authService.refreshAccessToken(email)
    }

    // Đăng nhập bằng Google
    @PostMapping("/google")
    fun googleLogin(@RequestParam("token") token: String): ApiResponse {
        return try {
            val jwt = authService.loginWithGoogle(token)
            ApiResponse("Login with Google successful", jwt)
        } catch (ex: Exception) {
            ApiResponse(ex.message ?: "Google login failed", null)
        }
    }

    @PostMapping("/verifyOtp")
    fun verifyOtp(@RequestParam username: String,@RequestParam email: String,@RequestParam address: String, @RequestParam otp: String, @RequestParam password: String, @RequestParam dateOfBirth: LocalDate, @RequestParam phone: String, @RequestParam school: String): ApiResponse {
        val storedOtp = redisService.getOtp(email)

        return if (storedOtp != null && storedOtp == otp) {
            try {
                // OTP hợp lệ, thực hiện đăng ký với mật khẩu
                val registerRequest = RegisterRequest(username, email, address, dateOfBirth, phone, password, school )

                // Kiểm tra nếu email đã tồn tại trong hệ thống
                if (userRepository.existsByEmail(email)) {
                    return ApiResponse("Email already exists", null)
                }

                // Đăng ký người dùng và lấy token
                val authResponse = authService.register(registerRequest)

                // Trả về thông báo thành công và thông tin token
                return ApiResponse("Registration successful", authResponse)
            } catch (ex: Exception) {
                return ApiResponse("Registration failed: ${ex.message}", null)
            }
        } else {
            return ApiResponse("Invalid OTP", null)
        }
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/change-password")
    fun changePassword(
        @RequestParam email: String,
        @RequestParam oldPassword: String,
        @RequestParam newPassword: String
    ): ApiResponse {
        return try {
            authService.changePassword(email, oldPassword, newPassword)
        } catch (e: Exception) {
            ApiResponse("An error occurred", null)
        }
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    fun logout(@RequestParam email: String): ApiResponse {
        return authService.logout(email)
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    fun getCurrentUser(@RequestHeader("Authorization") authHeader: String): ResponseEntity<UserDto> {
        val token = authHeader.removePrefix("Bearer ").trim()
        val userInfo = authService.getUserInfoFromAccessToken(token)
        return ResponseEntity.ok(userInfo)
    }

    private fun generateOtp(): String {
        return (100000..999999).random().toString()

    }

}
