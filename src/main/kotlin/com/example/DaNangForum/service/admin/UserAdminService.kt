package com.example.DaNangForum.service.admin

import com.example.DaNangForum.dto.ApiResponse
import com.example.DaNangForum.dto.admin.UserAdminDTO
import com.example.DaNangForum.repository.admin.UserAdminRepository
import com.example.danangforum.model.AuthProvider
import com.example.danangforum.model.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserAdminService (
    private val userAdminRepository: UserAdminRepository,
    adminRepository: UserAdminRepository
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
            avatar = user.avatar,
            dateOfBirth = user.dateOfBirth,
            phoneNumber = user.phoneNumber,
            role = user.role
        )

        return ResponseEntity.ok(userAdminDTO)
    }

    fun createUser(userAdminDTO: UserAdminDTO): ResponseEntity<UserAdminDTO>{
        val newUser = User(
            username = userAdminDTO.name,
            email = userAdminDTO.email,
            role = userAdminDTO.role, // Có thể tùy chỉnh role ở đây
            school = userAdminDTO.school,
            avatar = userAdminDTO.avatar,
            phoneNumber = userAdminDTO.phoneNumber,
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
        if(user == null){
            return ResponseEntity.notFound().build()
        }

        if(user.role == "ADMIN"){
            user.role = "USER"
        }else{
            user.role = "ADMIN"
        }

        return ResponseEntity(ApiResponse("change role",user.role), HttpStatus.OK)
    }

    fun deleteUser(userId: Long): ResponseEntity<ApiResponse>{
        userAdminRepository.deleteById(userId)
        return ResponseEntity.ok().build()
    }
}