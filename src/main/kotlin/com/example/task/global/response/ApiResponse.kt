package com.example.task.global.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: ErrorDetail? = null,
    val metadata: Map<String, Any>? = null
) {
    companion object {
        fun <T> success(data: T, metadata: Map<String, Any>? = null): ApiResponse<T> {
            return ApiResponse(success = true, data = data, metadata = metadata)
        }

        fun success(): ApiResponse<Unit> {
            return ApiResponse(success = true)
        }

        fun <T> error(code: String, message: String): ApiResponse<T> {
            return ApiResponse(success = false, error = ErrorDetail(code, message))
        }
    }
}

data class ErrorDetail(
    val code: String,
    val message: String
)