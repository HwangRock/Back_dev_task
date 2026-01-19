package com.example.task.presentation.admin

import com.example.task.application.admin.AdminReportService
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.nio.charset.StandardCharsets

@RestController
@RequestMapping("/admin/report")
class AdminReportController(
    private val adminReportService: AdminReportService
) {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/daily/csv")
    fun downloadDailyCsv(): ResponseEntity<ByteArrayResource> {
        val csv = adminReportService.generateDailyCsv()
        val resource = ByteArrayResource(csv.toByteArray(StandardCharsets.UTF_8))

        return ResponseEntity.ok()
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=daily_chat_report.csv"
            )
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(resource)
    }
}
