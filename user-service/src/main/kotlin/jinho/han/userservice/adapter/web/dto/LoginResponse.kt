package jinho.han.userservice.adapter.web.dto

import java.time.LocalDateTime

data class LoginResponse(
    val token: String,
    val user: UserResponse
)

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(user: jinho.han.userservice.domain.User): UserResponse = UserResponse(
            id = user.id!!,
            name = user.name,
            email = user.email,
            createdAt = user.createdAt
        )
    }
}
