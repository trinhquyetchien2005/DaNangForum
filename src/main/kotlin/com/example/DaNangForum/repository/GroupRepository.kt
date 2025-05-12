package com.example.DaNangForum.repository

import com.example.danangforum.model.Group
import com.example.danangforum.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface GroupRepository: JpaRepository<Group, Long> {
    fun findGroupsByOwner(owner: User): List<Group>
}