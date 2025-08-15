package jinho.han.userservice.dto.request

import jakarta.validation.constraints.NotBlank

class PasswordChangeRequest(
    val id: Int,

    @field:NotBlank("Password is required")
    val currentPassword: String,

    @field:NotBlank("Password is required")
    val newPassword: String
)