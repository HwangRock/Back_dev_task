package com.example.task.global.exeception

import org.springframework.http.HttpStatus

enum class ErrorCode(val httpStatus: HttpStatus, val code: String, val message: String) {
    // Auth
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A401", "인증되지 않은 사용자입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "A403", "권한이 없습니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "A401", "로그인에 실패했습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "A400", "이미 존재하는 이메일입니다."),

    // Chat
    CHAT_NOT_FOUND(HttpStatus.NOT_FOUND, "C404", "대화를 찾을 수 없습니다."),
    THREAD_NOT_FOUND(HttpStatus.NOT_FOUND, "C404", "스레드를 찾을 수 없습니다."),
    INVALID_AI_RESPONSE(HttpStatus.INTERNAL_SERVER_ERROR, "C500", "AI 응답 처리에 실패했습니다."),

    // Feedback
    FEEDBACK_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "F400", "이미 해당 대화에 피드백을 남겼습니다."),
    FEEDBACK_NOT_FOUND(HttpStatus.NOT_FOUND, "F404", "피드백을 찾을 수 없습니다."),

    // Global
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "G400", "올바르지 않은 입력값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G500", "서버 내부 오류가 발생했습니다.")
}

class BusinessException(val errorCode: ErrorCode) :
    RuntimeException(errorCode.message)
