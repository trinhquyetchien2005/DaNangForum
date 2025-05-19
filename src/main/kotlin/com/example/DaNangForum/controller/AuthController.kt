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
import com.google.protobuf.Api
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.mail.Address
import jakarta.mail.MessagingException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
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
    fun register(@RequestBody registerRequest: RegisterRequest): ResponseEntity<ApiResponse> {

            val otp = sendOtp(registerRequest.email)
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse("", otp))
    }

    @PostMapping("/forgotPassword")
    fun forgotPassword(@RequestParam email: String): ResponseEntity<ApiResponse> {
        val user = userRepository.findByEmail(email)
        ?: throw UsernameNotFoundException("User not found with email: $email")

        if (user.provider == AuthProvider.GOOGLE){
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(ApiResponse("Provider GOOGLE", null))
        }

        val otp = generateOtp() // Hàm tự tạo OTP
        val subject = "Your OTP refresh password"
        val body = "Your OTP refresh password is: $otp"

        emailService.sendEmail(email, subject, body)

        redisService.saveOtp(email, otp)

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse("Otp sent", null))
    }

    @PatchMapping("/verifyOtpPassword")
    fun verifyOtpPassword(@RequestParam email: String, @RequestParam otp: String): Any {
        return authService.refreshPassword(email, otp)
    }

    // Gửi mã OTP
    @PostMapping("/sendOtp")
    @Throws(MessagingException::class)
    fun sendOtp(@RequestParam email: String): ResponseEntity<ApiResponse> {
        try {
            val otp = generateOtp() // Hàm tự tạo OTP
            val subject = "Your OTP Code"
            val body = "Your OTP code is: $otp"

            emailService.sendEmail(email, subject, body)

            // Lưu OTP vào Redis (hoặc database)
            redisService.saveOtp(email, otp)

            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse("Otp sent", otp))
        }catch (e: MessagingException){
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse("Error", e))
        }
    }

    // Tạo OTP ngẫu nhiên


    @PostMapping("/login")
    fun login(@RequestParam email: String, @RequestParam password: String):ResponseEntity<AuthResponse> {
        return authService.login(LoginRequest(email, password))
    }

    @PostMapping("/refresh")
    fun refreshToken(@RequestParam email: String): ResponseEntity<ApiResponse> {
        return authService.refreshAccessToken(email)
    }

    // Đăng nhập bằng Google
    @PostMapping("/google")
    fun googleLogin(@RequestParam("token") token: String): ResponseEntity<AuthResponse> {
        return authService.loginWithGoogle(token)
    }

    @PostMapping("/verifyOtp")
    fun verifyOtp(@RequestParam username: String,@RequestParam email: String,@RequestParam address: String, @RequestParam otp: String, @RequestParam password: String, @RequestParam dateOfBirth: LocalDate, @RequestParam phone: String, @RequestParam school: String): ResponseEntity<ApiResponse> {
        val storedOtp = redisService.getOtp(email)

        return if (storedOtp != null && storedOtp == otp) {
            try {
                // OTP hợp lệ, thực hiện đăng ký với mật khẩu
                val registerRequest = RegisterRequest(username, email, address, dateOfBirth, phone, password, school )

                // Kiểm tra nếu email đã tồn tại trong hệ thống
                if (userRepository.existsByEmail(email)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse("User already exists", null))
                }

                // Đăng ký người dùng và lấy token
                val authResponse = authService.register(registerRequest)

                // Trả về thông báo thành công và thông tin token
                return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse("Registration successful", authResponse))
            } catch (ex: Exception) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse("Registration failed", null))
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse("Otp not true", null))
        }
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/change-password")
    fun changePassword(
        @RequestParam oldPassword: String,
        @RequestParam newPassword: String
    ): ResponseEntity<ApiResponse> {
        return authService.changePassword(oldPassword, newPassword)
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    fun logout(): ResponseEntity<ApiResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        val email = auth.name
        return authService.logout(email)
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    fun getCurrentUser(): ResponseEntity<ApiResponse> {
        return authService.getUserInfoFromAccessToken()
    }

    private fun generateOtp(): String {
        return (100000..999999).random().toString()
    }

}
