package com.example.DaNangForum.controller

import com.example.DaNangForum.dto.ApiResponse
import com.example.DaNangForum.dto.admin.UserAdminDTO
import com.example.DaNangForum.service.admin.UserAdminService
import com.example.DaNangForum.service.user.UserService
import com.example.danangforum.model.User
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/admin/user")
class UserAdminController(
    private val userAdminService: UserAdminService,
) {
    @GetMapping("/all")
    fun getAll(): ResponseEntity<List<User>> {
        return userAdminService.getAll()
    }

    @GetMapping("/UserId")
    fun getUserById(userId: Long): ResponseEntity<UserAdminDTO> {
        return userAdminService.getUser(userId)
    }

    @PutMapping("/role")
    fun updateUserRoles(userID: Long): ResponseEntity<ApiResponse> {
        return userAdminService.updateUser(userID)
    }

    @DeleteMapping("/userId")
    fun deleteUserById(userId: Long): ResponseEntity<ApiResponse> {
        return userAdminService.deleteUser(userId)
    }

    @PostMapping("/create")
    fun createUser(@RequestBody userAdminDTO: User): ResponseEntity<User> {
        return userAdminService.createUser(userAdminDTO)
    }
}
