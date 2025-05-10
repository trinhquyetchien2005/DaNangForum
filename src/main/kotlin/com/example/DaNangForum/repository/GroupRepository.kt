package com.example.DaNangForum.repository

import com.example.danangforum.model.Group
import org.springframework.data.jpa.repository.JpaRepository

interface GroupRepository: JpaRepository<Group, Long> {

}