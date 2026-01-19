package com.example.task.global.exeception

import com.example.task.global.response.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(javaClass)

    // 로그 예시 : [WARN] Business Exception: A005 - 이미 존재하는 이메일입니다.
    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(e: BusinessException): ResponseEntity<ApiResponse<Unit>> {
        logger.warn("Business Exception: ${e.errorCode.code} - ${e.message}")
        return ResponseEntity
            .status(e.errorCode.httpStatus)
            .body(ApiResponse.error(e.errorCode.code, e.errorCode.message))
    }

    // 로그 예시 : [WARN] Validation Exception: email: 올바른 이메일 형식이 아닙니다., password: size must be between 4 and 2147483647
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Unit>> {
        val errorMessage = e.bindingResult.fieldErrors.joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
        logger.warn("Validation Exception: $errorMessage")
        return ResponseEntity
            .status(ErrorCode.INVALID_INPUT.httpStatus)
            .body(ApiResponse.error(ErrorCode.INVALID_INPUT.code, errorMessage))
    }

    // 로그 예시 : [ERROR] Unexpected Exception: java.lang.NullPointerException: null ... (stack trace)
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Unit>> {
        logger.error("Unexpected Exception: ", e)
        return ResponseEntity
            .status(ErrorCode.INTERNAL_SERVER_ERROR.httpStatus)
            .body(ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR.code, ErrorCode.INTERNAL_SERVER_ERROR.message))
    }
}