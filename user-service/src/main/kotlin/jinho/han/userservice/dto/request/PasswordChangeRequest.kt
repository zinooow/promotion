package jinho.han.userservice.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

class PasswordChangeRequest(
    val id: Int,

    @field:NotBlank("Password is required")
    val currentPassword: String,

    @field:NotBlank("Password is required")
    @field:Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
        message = "Password must be at least 8 characters long and contain both letters and numbers")
    val newPassword: String
)