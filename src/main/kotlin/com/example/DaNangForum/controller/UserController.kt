package com.example.DaNangForum.controller

import com.example.DaNangForum.dto.ApiResponse
import com.example.DaNangForum.dto.user.UserUpdateRequest
import com.example.DaNangForum.repository.UserRepository
import com.example.DaNangForum.service.user.UserService
import com.example.danangforum.model.User
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userRepository: UserRepository,
    private val userService: UserService
){
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/update")
    fun updateUser(@RequestBody request: UserUpdateRequest): ResponseEntity<User> {
        return userService.updateUser(request)
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/remove")
    fun removeAccount(): ResponseEntity<ApiResponse> {
        return userService.removeAccount()
    }

    @PostMapping("/search/{name}")
    fun searchUser(@PathVariable("name") name: String): ResponseEntity<List<User>> {
        return userService.searchByUserName(name)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable("id") id: Long): ResponseEntity<User> {
        return userService.getUserById(id)
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/info")
    fun getUser(): ResponseEntity<User> {
        return userService.userInfo()
    }
}