package com.example.DaNangForum.repository

import com.example.danangforum.model.Group
import com.example.danangforum.model.GroupMember
import com.example.danangforum.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface GroupMemberRepository: JpaRepository<GroupMember, Long> {

    fun findByGroupAndMember(group: Group, member: User): GroupMember?
    fun findGroupMembersByGroup(group: Group): List<GroupMember>
    fun findGroupMembersByMember(member: User): List<GroupMember>
}