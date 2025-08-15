package jinho.han.userservice.application

import jinho.han.userservice.domain.User
import jinho.han.userservice.domain.UserLoginHistory
import jinho.han.userservice.repository.UserLoginHistoryRepository
import jinho.han.userservice.repository.UserRepository
import jinho.han.userservice.dto.request.PasswordChangeRequest
import jinho.han.userservice.dto.request.LoginRequest
import jinho.han.userservice.dto.request.SignupRequest
import jinho.han.userservice.dto.request.UpdateRequest
import jinho.han.userservice.application.exception.UnauthorizedException
import jinho.han.userservice.application.exception.UserDuplicateException
import jinho.han.userservice.application.exception.UserNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    val userRepository: UserRepository,
    val loginHistoryRepository: UserLoginHistoryRepository,
    val passwordEncoder: PasswordEncoder
) {

    fun createUser(email: String, password: String, name: String): User {
        if(userRepository.findByEmail(email).isPresent) { throw UserDuplicateException("해당 이메일을 가진 유저가 이미 존재합니다.")}

        val user = User.create(email, password, name, passwordEncoder)

        return userRepository.save(user)
    }
    fun authenticate(request: LoginRequest): User {
        val user = userRepository.findByEmail(request.email).orElseThrow { UserNotFoundException("이메일이 올바르지 않아 로그인 할 수 없습니다.") }
        if(user.passwordHash != passwordEncoder.encode(request.password)) { throw UnauthorizedException("비밀번호가 올바르지 않습니다.")
        }
        return user
    }

    fun getUserById(userId: Int): User {
        return userRepository.findById(userId).orElseThrow { UserNotFoundException("유저를 찾을 수 없습니다.") }
    }

    @Transactional
    fun updateUser(userId: Int, name: String): User {
        val user = getUserById(userId)
        user.name = name
        return userRepository.save(user)
    }

    @Transactional
    fun changePassword(userId: Int, currentPassword: String, newPassword: String): User {
        val user = getUserById(userId)
        if(user.passwordHash != passwordEncoder.encode(currentPassword)) { throw UnauthorizedException("Invalid current password")
        }
        user.passwordHash = passwordEncoder.encode(newPassword)
        return userRepository.save(user)
    }

    fun getLoginHistory(userId: Int): List<UserLoginHistory> {
        return loginHistoryRepository.findByUserOrderByLoginTimeDesc(getUserById(userId))
    }

}


