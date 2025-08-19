package jinho.han.couponservice.adapter.web.dto

data class ApiResponse<T>(
    val message: String,
    val code: Int,
    val data: T? = null
)
