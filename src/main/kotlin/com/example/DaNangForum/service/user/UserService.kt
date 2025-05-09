package com.example.DaNangForum.service.user

import com.example.DaNangForum.dto.ApiResponse
import com.example.DaNangForum.dto.user.UserUpdateRequest
import com.example.DaNangForum.repository.UserRepository
import com.example.DaNangForum.service.auth.RedisService
import com.example.danangforum.model.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody

@Service
class UserService(
    private val userRepository: UserRepository,
    private val redisService: RedisService
) {
    fun updateUser(@RequestBody requestUser: UserUpdateRequest): ResponseEntity<User> {
        val auth = SecurityContextHolder.getContext().authentication
        val emailfromtoken = auth.name
        val user = userRepository.findByEmail(emailfromtoken)?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)

        val userOptional = userRepository.findByEmail(emailfromtoken) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)

        userOptional.username = requestUser.name
        userOptional.bio = requestUser.bio
        userOptional.address = requestUser.address
        userOptional.avatar = requestUser.avatar
        userOptional.dateOfBirth = requestUser.dateOfBirth
        userOptional.school = requestUser.school

        val updatedUser = userRepository.save(user)

        return ResponseEntity.status(HttpStatus.OK).body(updatedUser)
    }

    fun removeAccount(): ResponseEntity<ApiResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        val emailfromtoken = auth.name
        val user = userRepository.findByEmail(emailfromtoken)?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)

        redisService.deleteToken(user.email)
        userRepository.deleteByEmail(user.email)

        return ResponseEntity.status(HttpStatus.OK).body(null)
    }

    fun searchByUserName(username: String): ResponseEntity<List<User>> {
        val listUser = userRepository.searchByUsernameLike(username)
        if (listUser.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        return ResponseEntity.status(HttpStatus.OK).body(listUser)
    }

    fun getUserById(id: Long): ResponseEntity<User> {
        val user = userRepository.findById(id).orElse(null)
        ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)

        return ResponseEntity.status(HttpStatus.OK).body(user)
    }
}