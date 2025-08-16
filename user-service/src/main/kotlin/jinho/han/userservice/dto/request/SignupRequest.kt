package jinho.han.userservice.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class SignupRequest(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,

    @field:NotBlank(message = "Password is required")
    @field:Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
        message = "Password must be at least 8 characters long and contain both letters and numbers")
    val password: String,

    @field:NotBlank(message = "Name is required")
    val name: String
)