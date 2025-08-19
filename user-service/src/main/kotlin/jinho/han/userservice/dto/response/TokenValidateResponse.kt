package jinho.han.userservice.dto.response

data class TokenValidateResponse(
    val email: String,
    val name: String,
    val valid: Boolean,
    val role: String,
)
