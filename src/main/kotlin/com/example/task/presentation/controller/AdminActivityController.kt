package com.example.task.presentation.admin

import com.example.task.application.admin.AdminActivityService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/activity")
class AdminActivityController(
    private val adminActivityService: AdminActivityService
) {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/daily")
    fun getDailyActivity() =
        adminActivityService.getDailyStats()
}
