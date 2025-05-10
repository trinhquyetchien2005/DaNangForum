package com.example.DaNangForum.controller

import com.example.DaNangForum.service.Group.GroupService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/group")
class GroupController (
    groupService: GroupService
){

}