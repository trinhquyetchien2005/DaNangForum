package com.example.DaNangForum.service.auth

import org.springframework.stereotype.Service
import redis.clients.jedis.Jedis

@Service
class RedisService {

    private val jedis = Jedis("localhost", 6379)

    fun saveOtp(email: String, otp: String) {
        jedis.setex(email, 300, otp)
    }

    fun getOtp(email: String): String? {
        return jedis.get(email)
    }

    fun saveToken(email: String, refreshToken: String) {
        jedis.setex("$email-refresh", 3600, refreshToken)
    }

    fun getToken(email: String): String? {
        return jedis.get("$email-refresh")
    }
}
