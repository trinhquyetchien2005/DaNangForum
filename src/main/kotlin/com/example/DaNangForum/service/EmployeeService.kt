package com.example.DaNangForum.service

import com.example.DaNangForum.model.Employee
import com.example.DaNangForum.repository.EmployeeRepository
import org.springframework.stereotype.Service

@Service
class EmployeeService(private val repository: EmployeeRepository) {
    fun getAllEmployees(): List<Employee> = repository.findAll()
}
