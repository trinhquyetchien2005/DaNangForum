package com.example.DaNangForum.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.MailException
import java.util.Properties

@Configuration
class EmailConfig {

    @Bean
    fun javaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()

        // Lấy thông tin cấu hình từ application.properties
        mailSender.host = "smtp.gmail.com"
        mailSender.port = 587
        mailSender.username = "trinhquyetchien2005@gmail.com"
        mailSender.password = "qauwhflvhpepmusg"

        val props = Properties()
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.ssl.trust"] = "smtp.gmail.com"

        mailSender.javaMailProperties = props
        return mailSender
    }
}
