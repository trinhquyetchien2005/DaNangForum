package com.example.DaNangForum.repository
import com.example.DaNangForum.model.Employee
import org.springframework.data.jpa.repository.JpaRepository

interface EmployeeRepository : JpaRepository<Employee, Long>
