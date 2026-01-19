package com.example.task.presentation.controller

import com.example.task.application.UserService
import com.example.task.global.response.ApiResponse
import com.example.task.presentation.dto.AuthResponse
import com.example.task.presentation.dto.LoginRequest
import com.example.task.presentation.dto.SignupRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@Tag(name = "User", description = "사용자 관리 API")
@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @Operation(summary = "회원 가입", description = "새로운 사용자를 등록합니다.")
    @PostMapping("/signup")
    fun signup(
        @Valid @RequestBody request: SignupRequest
    ): ApiResponse<Unit> {

        userService.signup(request)
        return ApiResponse.success()
    }


    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인하여 JWT 토큰을 발급받습니다.")
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ApiResponse<AuthResponse> {
        val response = userService.login(request)
        return ApiResponse.success(response)
    }
}