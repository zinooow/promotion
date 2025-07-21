package jinho.han.userservice.adapter.repository

import jinho.han.userservice.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>
}