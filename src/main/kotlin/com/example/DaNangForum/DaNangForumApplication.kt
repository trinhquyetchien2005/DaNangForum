package com.example.DaNangForum

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.web.bind.annotation.RequestMapping

@SpringBootApplication
@EnableJpaAuditing
@EntityScan("com.example.danangforum.model")
class DaNangForumApplication

fun main(args: Array<String>) {
	runApplication<DaNangForumApplication>(*args)
}


