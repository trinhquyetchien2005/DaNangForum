package com.example.DaNangForum.security

import com.example.DaNangForum.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("User not found")

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.email)
            .password(user.password?: "") // Phải là mật khẩu đã mã hóa
            .roles(user.role)        // hoặc .authorities(...) nếu bạn dùng authorities
            .build()
    }
}

