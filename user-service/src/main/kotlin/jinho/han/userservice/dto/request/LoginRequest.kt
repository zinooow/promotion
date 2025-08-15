package jinho.han.userservice.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank("Email is required")
    @field:Email("Invalid email format")
    val email: String,

    @field:NotBlank("Password is required")
    val password: String
)