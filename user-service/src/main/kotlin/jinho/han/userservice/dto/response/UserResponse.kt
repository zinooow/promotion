package jinho.han.userservice.dto.response

import jinho.han.userservice.domain.User
import java.time.LocalDateTime

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(user: User): UserResponse = UserResponse(
            id = user.id!!,
            name = user.name,
            email = user.email,
            createdAt = user.createdAt
        )
    }
}