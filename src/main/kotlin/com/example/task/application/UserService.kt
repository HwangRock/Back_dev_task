package com.example.task.application

import com.example.task.Interface.UserRepository
import com.example.task.domain.User
import com.example.task.global.config.secutiry.JwtProvider
import com.example.task.global.exeception.BusinessException
import com.example.task.global.exeception.ErrorCode
import com.example.task.presentation.dto.AuthResponse
import com.example.task.presentation.dto.LoginRequest
import com.example.task.presentation.dto.SignupRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtProvider: JwtProvider
) {

    @Transactional
    fun signup(request: SignupRequest): AuthResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS)
        }

        val user = User(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            name = request.name,
            role = request.role
        )
        val savedUser = userRepository.save(user)

        val token = jwtProvider.createToken(savedUser.email, savedUser.role)
        return AuthResponse(savedUser.email, savedUser.name, savedUser.role, token)
    }

    @Transactional(readOnly = true)
    fun login(request: LoginRequest): AuthResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw BusinessException(ErrorCode.LOGIN_FAILED)

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw BusinessException(ErrorCode.LOGIN_FAILED)
        }

        val token = jwtProvider.createToken(user.email, user.role)
        return AuthResponse(user.email, user.name, user.role, token)
    }
}