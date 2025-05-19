package com.example.DaNangForum.service.admin

import com.example.DaNangForum.repository.admin.PostAdminRepository
import com.example.DaNangForum.repository.admin.UserAdminRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class DashboardAdminService (
    private val userAdminRepository: UserAdminRepository,
    private val postAdminRepository: PostAdminRepository
){
    fun countUsers(): ResponseEntity<Long>{
        val users = userAdminRepository.count()
        return ResponseEntity.status(200).body(users)
    }
    fun countPosts(): ResponseEntity<Long>{
        val posts = postAdminRepository.count()

        return ResponseEntity.status(200).body(posts)
    }
    fun countUsersCreatedToday(): ResponseEntity<Long>{
        val userNumber = userAdminRepository.countByDate(LocalDate.now())
        if ( userNumber> 0){
            return ResponseEntity.status(200).body(userNumber)
        }else{
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
        }
    }
    fun countPostsCreatedToday(): ResponseEntity<Long>{
        val postNUmber = postAdminRepository.countByDate(LocalDate.now())
        if ( postNUmber > 0){
            return ResponseEntity.status(200).body(postNUmber)
        }else{
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
        }
    }


}