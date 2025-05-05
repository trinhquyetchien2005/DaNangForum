package com.example.DaNangForum.service.email

import jakarta.mail.MessagingException
import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.mail.MailException
import org.springframework.stereotype.Service


@Service
class EmailService(@Autowired private val javaMailSender: JavaMailSender) {

    @Throws(MailException::class, MessagingException::class)
    fun sendEmail(to: String, subject: String, body: String) {
        val mimeMessage: MimeMessage = javaMailSender.createMimeMessage()

        val messageHelper = MimeMessageHelper(mimeMessage, true)
        messageHelper.setFrom("trinhquyetchien2005@gmail.com")
        messageHelper.setTo(to)
        messageHelper.setSubject(subject)
        messageHelper.setText(body, true)

        javaMailSender.send(mimeMessage)
    }
}
