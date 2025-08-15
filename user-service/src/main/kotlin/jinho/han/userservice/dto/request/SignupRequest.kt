package jinho.han.userservice.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class SignupRequest(
    @field:NotBlank("Email is required")
    @field:Email("Invalid email format")
    val email: String,

    @field:NotBlank("Password is required")
    @field:Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
        message = "Password must be at least 8 characters long and contain both letters and numbers")
    val password: String,

    @field:NotBlank("Name is required")
    val name: String
)