package jinho.han.userservice.dto.response

data class LoginResponse(
    val token: String,
    val user: UserResponse
)

