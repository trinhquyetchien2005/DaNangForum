package com.example.DaNangForum.service.Group

import com.example.DaNangForum.dto.ApiResponse
import com.example.DaNangForum.dto.Group.GroupDto
import com.example.DaNangForum.repository.GroupRepository
import com.example.DaNangForum.repository.UserRepository
import com.example.DaNangForum.repository.GroupMemberRepository
import com.example.danangforum.model.Group
import com.example.danangforum.model.GroupMember
import com.example.danangforum.model.MemberStatus
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class GroupService (
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository,
    private val groupMemberRepository: GroupMemberRepository
){
    fun create(group: GroupDto): ResponseEntity<ApiResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        val email = auth.name
        val user = userRepository.findByEmail(email)

        if (user == null) {
            return ResponseEntity.status(401).body(ApiResponse("vui long dang nhap", null))
        }

        val newGroup = Group(
            owner = user,
            groupname = group.groupname,
            createAt = LocalDateTime.now(),
        )

        groupRepository.save(newGroup)

        return ResponseEntity.status(201).body(ApiResponse("success", newGroup))
    }

    fun delete(groupId: Long): ResponseEntity<ApiResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        val email = auth.name
        val user = userRepository.findByEmail(email)
        val group = groupRepository.findById(groupId).orElse(null)?:return ResponseEntity.status(404).body(ApiResponse("nhom ko ton tai", null))

        if(user != group.owner){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse("ban khong phai chu nhom nay", null))
        }
        groupRepository.delete(group)

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse("Successfully removed this group", group))
    }

    fun changeName(groupId: Long, name: String): ResponseEntity<ApiResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        val email = auth.name
        val user = userRepository.findByEmail(email)

        val group = groupRepository.findById(groupId).orElse(null)?:return ResponseEntity.status(404).body(ApiResponse("nhom ko ton tai", null))

        if(user != group.owner){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse("ban khong phai chu nhom nay", null))
        }

        group.groupname = name
        groupRepository.save(group)
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse("success", group.groupname))
    }

    fun confirmMember(groupId: Long, userId: Long): ResponseEntity<ApiResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        val email = auth.name
        val owner = userRepository.findByEmail(email)
        val group = groupRepository.findById(groupId).orElse(null)
            ?: return ResponseEntity.status(404).body(ApiResponse("Group not found", null))

        if (group.owner != owner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse("Bạn không phải chủ nhóm", null))
        }

        val member = userRepository.findById(userId).orElse(null)
            ?: return ResponseEntity.status(404).body(ApiResponse("Người dùng không tồn tại", null))

        val groupMember = groupMemberRepository.findByGroupAndMember(group, member)
            ?: return ResponseEntity.status(404).body(ApiResponse("Người dùng chưa gửi yêu cầu tham gia nhóm", null))

        if (groupMember.status != MemberStatus.PENDING) {
            return ResponseEntity.status(400).body(ApiResponse("Người dùng đã được xác nhận hoặc bị từ chối", null))
        }

        groupMember.status = MemberStatus.APPROVED
        groupMemberRepository.save(groupMember)

        return ResponseEntity.ok(ApiResponse("Đã xác nhận thành viên", null))
    }

    fun denyMember(groupId: Long, userId: Long): ResponseEntity<ApiResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        val email = auth.name
        val owner = userRepository.findByEmail(email)

        val group = groupRepository.findById(groupId).orElse(null)
            ?: return ResponseEntity.status(404).body(ApiResponse("Group không tồn tại", null))

        if (group.owner != owner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse("Bạn không phải chủ nhóm", null))
        }

        val member = userRepository.findById(userId).orElse(null)
            ?: return ResponseEntity.status(404).body(ApiResponse("Người dùng không tồn tại", null))

        val groupMember = groupMemberRepository.findByGroupAndMember(group, member)
            ?: return ResponseEntity.status(404).body(ApiResponse("Người dùng không nằm trong nhóm", null))

        if (groupMember.status != MemberStatus.PENDING) {
            return ResponseEntity.status(400).body(ApiResponse("Người dùng không ở trạng thái chờ", null))
        }

        groupMember.status = MemberStatus.REJECTED
        groupMemberRepository.save(groupMember)

        return ResponseEntity.ok(ApiResponse("Đã từ chối thành viên", null))
    }

    fun deleteMember(groupId: Long, userId: Long): ResponseEntity<ApiResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        val email = auth.name
        val owner = userRepository.findByEmail(email)

        val group = groupRepository.findById(groupId).orElse(null)
            ?: return ResponseEntity.status(404).body(ApiResponse("Group không tồn tại", null))

        if (group.owner != owner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse("Bạn không phải chủ nhóm", null))
        }

        val member = userRepository.findById(userId).orElse(null)
            ?: return ResponseEntity.status(404).body(ApiResponse("Người dùng không tồn tại", null))

        val groupMember = groupMemberRepository.findByGroupAndMember(group, member)
            ?: return ResponseEntity.status(404).body(ApiResponse("Người dùng không nằm trong nhóm", null))

        groupMemberRepository.delete(groupMember)

        return ResponseEntity.ok(ApiResponse("Đã xóa thành viên khỏi nhóm", null))
    }

    fun requestJoinGroup(groupId: Long): ResponseEntity<ApiResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        val email = auth.name
        val user = userRepository.findByEmail(email)?:return ResponseEntity.notFound().build()

        val group = groupRepository.findById(groupId).orElse(null)
            ?: return ResponseEntity.status(404).body(ApiResponse("Group không tồn tại", null))

        // Kiểm tra xem đã gửi yêu cầu/chấp nhận trước chưa
        val existing = groupMemberRepository.findByGroupAndMember(group, user)
        if (existing != null) {
            return ResponseEntity.status(400).body(ApiResponse("Bạn đã gửi yêu cầu hoặc đang là thành viên", null))
        }

        if(user == group.owner) {
            return ResponseEntity.status(400).body(ApiResponse("Bạn là chủ nhóm này", null))
        }

        val newRequest = GroupMember(
            group = group,
            member = user,
            status = MemberStatus.PENDING
        )

        groupMemberRepository.save(newRequest)

        return ResponseEntity.ok(ApiResponse("Đã gửi yêu cầu tham gia nhóm", null))
    }


}