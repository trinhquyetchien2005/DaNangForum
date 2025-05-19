package com.example.DaNangForum.service.admin

import com.example.DaNangForum.dto.ApiResponse
import com.example.DaNangForum.dto.admin.UserAdminDTO
import com.example.DaNangForum.repository.admin.UserAdminRepository
import com.example.danangforum.model.AuthProvider
import com.example.danangforum.model.User
import io.lettuce.core.KillArgs.Builder.user
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserAdminService (
    private val userAdminRepository: UserAdminRepository,
    adminRepository: UserAdminRepository,
    private val passwordEncoder: PasswordEncoder
){
    fun getAll(): ResponseEntity<List<User>> {
        return ResponseEntity(userAdminRepository.findAll(),HttpStatus.OK)
    }
    fun getUser(UserId: Long): ResponseEntity<UserAdminDTO>{
        val user = userAdminRepository.findById(UserId).orElse(null)

        if(user == null){
            return ResponseEntity.notFound().build()
        }

        val userAdminDTO = UserAdminDTO(
            name = user.username,
            email = user.email,
            school = user.school,
            address = user.address,
            bio = user.bio,
            dateOfBirth = user.dateOfBirth,
            phoneNumber = user.phoneNumber,
            role = user.role
        )

        return ResponseEntity.ok(userAdminDTO)
    }

    fun createUser(userAdminDTO: User): ResponseEntity<User>{
        val newUser = User(
            username = userAdminDTO.username,
            email = userAdminDTO.email,
            role = userAdminDTO.role,
            password = passwordEncoder.encode(userAdminDTO.password),
            school = userAdminDTO.school,
            phoneNumber = userAdminDTO.phoneNumber,
            avatar = "",
            bio = userAdminDTO.bio,
            address = userAdminDTO.address,
            create_at = LocalDateTime.now(),
            provider = AuthProvider.LOCAL // Đảm bảo provider là GOOGLE
        )
        userAdminRepository.save(newUser)

        return ResponseEntity.ok(userAdminDTO)
    }

    fun updateUser(UserId: Long): ResponseEntity<ApiResponse>{
        val user = userAdminRepository.findById(UserId).orElse(null)
            ?: return ResponseEntity.notFound().build()

        user.role = if (user.role == "ADMIN") "USER" else "ADMIN"
        userAdminRepository.save(user)    // <-- Lưu lại vào DB

        return ResponseEntity.ok(ApiResponse("Change role successful", user.role))
    }

    fun deleteUser(userId: Long): ResponseEntity<ApiResponse>{
        userAdminRepository.deleteById(userId)
        return ResponseEntity.ok().build()
    }
}