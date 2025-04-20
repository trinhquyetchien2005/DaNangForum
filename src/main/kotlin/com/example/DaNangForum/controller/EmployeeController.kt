package com.example.DaNangForum.controller

import com.example.DaNangForum.model.Employee
import com.example.DaNangForum.service.EmployeeService
import jakarta.servlet.http.HttpSession
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/employees")
class EmployeeController(private val service: EmployeeService) {
    @GetMapping

    fun getAll(): List<Employee>{
       return service.getAllEmployees()
    }
}

