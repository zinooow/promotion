package jinho.han.userservice.application

import jinho.han.userservice.domain.User
import jinho.han.userservice.domain.UserLoginHistory
import jinho.han.userservice.adapter.repository.UserLoginHistoryRepository
import jinho.han.userservice.adapter.repository.UserRepository
import jinho.han.userservice.application.dto.UserPasswordUpdateRequest
import jinho.han.userservice.application.dto.LoginRequest
import jinho.han.userservice.application.dto.UserCreateRequest
import jinho.han.userservice.application.dto.UserUpdateRequest
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

    fun createUser(request: UserCreateRequest): User {
        if(userRepository.findByEmail(request.email).isPresent) { throw UserDuplicateException("해당 이메일을 가진 유저가 이미 존재합니다.")
        }
        var user = User.create(request, passwordEncoder)
        user = userRepository.save(user)
        return user
    }
    fun authenticate(request: LoginRequest): User {
        val user = userRepository.findByEmail(request.email).orElseThrow { UserNotFoundException("이메일이 올바르지 않아 로그인 할 수 없습니다.") }
        if(user.passwordHash != passwordEncoder.encode(request.password)) { throw UnauthorizedException("비밀번호가 올바르지 않습니다.")
        }
        return user
    }

    fun getUserById(userId: Long): User {
        return userRepository.findById(userId).orElseThrow { UserNotFoundException("유저를 찾을 수 없습니다.") }
    }

    @Transactional
    fun updateUser(request: UserUpdateRequest): User {
        val user = getUserById(request.id)
        user.name = request.name
        return userRepository.save(user)
    }

    @Transactional
    fun changePassword(userId: Long, request: UserPasswordUpdateRequest): User {
        val user = getUserById(userId)
        if(user.passwordHash != passwordEncoder.encode(request.currentPassword)) { throw UnauthorizedException("비밀번호가 일치하지 않습니다.")
        }
        user.passwordHash = passwordEncoder.encode(request.newPassword)
        return userRepository.save(user)
    }

    fun getLoginHistory(userId: Long): List<UserLoginHistory> {
        return loginHistoryRepository.findByUserOrderByLoginTimeDesc(getUserById(userId))
    }


}


