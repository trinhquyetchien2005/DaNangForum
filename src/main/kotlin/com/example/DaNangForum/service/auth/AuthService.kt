package com.example.DaNangForum.service.auth

import com.example.DaNangForum.dto.ApiResponse
import com.example.DaNangForum.dto.auth.AuthResponse
import com.example.DaNangForum.dto.auth.LoginRequest
import com.example.DaNangForum.dto.auth.RegisterRequest
import com.example.DaNangForum.dto.user.UserDto
import com.example.DaNangForum.repository.UserRepository
import com.example.DaNangForum.security.JwtUtils
import com.example.DaNangForum.service.email.EmailService
import com.example.danangforum.model.AuthProvider
import com.example.danangforum.model.User
import com.nimbusds.jose.shaded.gson.JsonObject
import com.nimbusds.jose.shaded.gson.JsonParser
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtils: JwtUtils,
    private val redisTemplate: RedisTemplate<String, String>,
    private val redisService: RedisService,
    private val emailService: EmailService,
) {

    fun register(request: RegisterRequest): ResponseEntity<AuthResponse> {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email đã tồn tại")
        }

        val newUser = User(
            username = request.username,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            role = "USER",
            school = request.school,
            avatar = "",
            phoneNumber = request.phone,
            dateOfBirth = request.dateOfBirth,
            bio = "",
            address = request.address,
            create_at = LocalDateTime.now(),
            provider = AuthProvider.LOCAL
        )

        userRepository.save(newUser)

        val accessToken = jwtUtils.generateAccessToken(newUser.email)
        val refreshToken = jwtUtils.generateRefreshToken(newUser.email)

        redisService.saveToken(newUser.email, refreshToken)

        return ResponseEntity.status(HttpStatus.CREATED).body(AuthResponse(accessToken, refreshToken))
    }

    fun login(request: LoginRequest): ResponseEntity<AuthResponse>  {

        val user = userRepository.findByEmail(request.email)
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AuthResponse(null, null))
            }

        // Kiểm tra tài khoản có phải tài khoản đăng nhập Google không
        if (user.provider != AuthProvider.LOCAL) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(AuthResponse(null, null))
        }

        // Kiểm tra mật khẩu
        if (!passwordEncoder.matches(request.password, user.password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AuthResponse(null, null))
        }
        // Tạo access token và refresh token
        val accessToken = jwtUtils.generateAccessToken(user.email)
        val refreshToken = jwtUtils.generateRefreshToken(user.email)

        redisService.saveToken(user.email, refreshToken)

        val userDto = UserDto(user.userId, user.email, user.username, user.avatar)

        // Trả về response
        return ResponseEntity.status(HttpStatus.OK).body(AuthResponse(accessToken, refreshToken, userDto))
    }

    fun loginWithGoogle(token: String): ResponseEntity<AuthResponse> {
        var newUser = User()
        try {
            // Xác minh token Google và lấy thông tin người dùng
            val googleUserInfo = verifyGoogleToken(token)

            if (googleUserInfo == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AuthResponse(null, null))
            }

            val email = googleUserInfo.get("email").asString

            // Kiểm tra xem người dùng đã tồn tại trong DB chưa
            val existingUser = userRepository.findByEmail(email)

            val jwtToken: String
            if (existingUser != null) {
                // Kiểm tra provider
                if (existingUser.provider != AuthProvider.GOOGLE) {
                    // Nếu người dùng đã tồn tại nhưng dùng provider khác, ném lỗi
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(AuthResponse(null, null))
                }
                // Nếu người dùng đã tồn tại và sử dụng Google, tạo JWT mới
                jwtToken = jwtUtils.generateAccessToken(existingUser.email)
            } else {
                // Nếu chưa tồn tại, tạo mới người dùng và trả về JWT
                newUser = User(
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

            val refreshToken = jwtUtils.generateRefreshToken(email)

            redisService.saveToken(email, refreshToken)

            val newUserDto = UserDto(newUser.userId, newUser.email, newUser.username, newUser.avatar,)

            return ResponseEntity.status(200).body(AuthResponse(jwtToken, refreshToken, newUserDto))

        } catch (e: Exception) {
            // Tạo OAuth2Error và ném OAuth2AuthenticationException nếu xác minh token thất bại
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AuthResponse(null, null))
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

    fun refreshAccessToken(email: String): ResponseEntity<ApiResponse> {
        val storedRefreshToken = redisService.getToken(email)

        if (storedRefreshToken == null) {
            return ResponseEntity.status(HttpStatus.GONE).body(ApiResponse("Refresh token het han", null))
        }

        // Tạo mới access token và refresh token
        val newAccessToken = jwtUtils.generateAccessToken(email)
        val newRefreshToken = jwtUtils.generateRefreshToken(email)

        redisService.saveToken(email, newRefreshToken)

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse("New access token", newAccessToken))
    }

    fun changePassword(email: String, oldPassword: String, newPassword: String): ResponseEntity<ApiResponse> {
        val user = userRepository.findByEmail(email)
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse("User not found", null))
        }

        if (user.provider == AuthProvider.GOOGLE) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse("Account is Google", null))
        }

        // Kiểm tra mật khẩu cũ và mật khẩu mới không trùng nhau
        if (oldPassword == newPassword) {
            return ResponseEntity.status(400).body(ApiResponse("Password does not match", null))
        }

        // Kiểm tra mật khẩu cũ có đúng không
        if (!passwordEncoder.matches(oldPassword, user.password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse("Password does not match", null))
        }

        user.password = passwordEncoder.encode(newPassword)
        userRepository.save(user)

        return return ResponseEntity.status(HttpStatus.OK).body(ApiResponse("Changed password", user.password))
    }

    fun getUserInfoFromAccessToken(): ResponseEntity<ApiResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        val userDetails = auth.name
        val user = userRepository.findByEmail(userDetails)

        return ResponseEntity.status(200).body(ApiResponse("info: ",user))
    }

    fun refreshPassword(email: String, otp: String): ResponseEntity<ApiResponse> {
        val otpS = redisService.getOtp(email)
        if(otpS == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse("Invalid otp", null))
        }

        if (otpS == otp){
            val user = userRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("User not found with email: $email")

            val newPassword = generatePassword()

            user.password = passwordEncoder.encode(newPassword)
            userRepository.save(user)

            val subject = "Your new password"
            val body = "Your new password is: $newPassword"

            emailService.sendEmail(email, subject, body)


            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse("Password changed successfully", null))
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse("Password does not match the current password", null))
    }

    fun logout(email: String): ResponseEntity<ApiResponse> {
        val user = userRepository.findByEmail(email)
        ?: throw UsernameNotFoundException("User not found with email: $email")

        redisService.deleteToken(email)
        return ResponseEntity.status(200).body(ApiResponse("logout", null))
    }
    fun generatePassword(length: Int = 10): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#\$%^&*()"
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }

}
