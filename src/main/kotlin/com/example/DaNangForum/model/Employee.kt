package com.example.DaNangForum.model

import jakarta.persistence.*

@Entity
@Table(name = "employee")
data class Employee(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String = "",

    val email: String = ""
)
