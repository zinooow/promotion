package jinho.han.userservice.repository

import jinho.han.userservice.domain.UserLoginHistory
import jinho.han.userservice.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserLoginHistoryRepository: JpaRepository<UserLoginHistory, Long> {
    fun findByUserOrderByLoginTimeDesc(user: User): List<UserLoginHistory>
}