package com.example.danangforum.model

import jakarta.persistence.*

@Entity
@Table(name = "notification")
open class Notification(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val notificationId: Long = 0,

    @Column(nullable = false)
    val title: String = "",

    @Lob
    @Column(nullable = false)
    val content: String = ""
) {
    constructor() : this(0, "", "")
}

