package com.example.DaNangForum.controller

import com.example.DaNangForum.dto.ApiResponse
import com.example.DaNangForum.dto.Group.GroupDto
import com.example.DaNangForum.service.Group.GroupService
import com.example.danangforum.model.Group
import com.example.danangforum.model.GroupMember
import com.example.danangforum.model.User
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/group")
class GroupController(
    private val groupService: GroupService
) {

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{groupId}/confirm/{userId}")
    fun confirmMember(
        @PathVariable groupId: Long,
        @PathVariable userId: Long
    ): ResponseEntity<ApiResponse> {
        return groupService.confirmMember(groupId, userId)
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{groupId}/deny/{userId}")
    fun denyMember(
        @PathVariable groupId: Long,
        @PathVariable userId: Long
    ): ResponseEntity<ApiResponse> {
        return groupService.denyMember(groupId, userId)
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{groupId}/member/{userId}")
    fun deleteMember(
        @PathVariable groupId: Long,
        @PathVariable userId: Long
    ): ResponseEntity<ApiResponse> {
        return groupService.deleteMember(groupId, userId)
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/delete/{groupId}")
    fun deleteGroup(@PathVariable groupId: Long): ResponseEntity<ApiResponse> {
        return groupService.delete(groupId)
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/create")
    fun createGroup(@RequestBody group: GroupDto): ResponseEntity<ApiResponse> {
        return groupService.create(group)
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/Jorn/{groupId}")
    fun joinGroup(@PathVariable groupId: Long): ResponseEntity<ApiResponse> {
        return groupService.requestJoinGroup(groupId)
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/changeName/{groupId}")
    fun changeName(groupId: Long, @RequestBody name: String): ResponseEntity<ApiResponse> {
        return groupService.changeName(groupId, name)
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/myGroup")
    fun getGroups(): ResponseEntity<List<Group>> {
        return groupService.getmyGroup()
    }

    @SecurityRequirement(name = "bearerAuth")
   @GetMapping("/myGroupMember/{groupId}")
    fun getGroupMembers(@PathVariable groupId: Long): ResponseEntity<List<User>> {
        return groupService.getMyGroupMembers(groupId)
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/myGroupJoin")
    fun getGroupJoin(): ResponseEntity<List<Group>> {
        return groupService.getmyGroupJoin()
    }
}
