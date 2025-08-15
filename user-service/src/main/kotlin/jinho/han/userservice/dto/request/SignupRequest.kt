package jinho.han.userservice.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class SignupRequest(
    @field:NotBlank("Email is required")
    @field:Email("Invalid email format")
    val email: String,

    @field:NotBlank("Password is required")
    val password: String,

    @field:NotBlank("Name is required")
    val name: String
)