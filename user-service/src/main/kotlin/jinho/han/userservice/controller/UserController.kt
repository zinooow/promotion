package jinho.han.userservice.controller

import jinho.han.userservice.application.UserService
import jinho.han.userservice.application.exception.DuplicateUserException
import jinho.han.userservice.application.exception.UnauthorizedAccessException
import jinho.han.userservice.application.exception.UserNotFoundException
import jinho.han.userservice.domain.User
import jinho.han.userservice.domain.UserLoginHistory
import jinho.han.userservice.dto.request.PasswordChangeRequest
import jinho.han.userservice.dto.request.SignupRequest
import jinho.han.userservice.dto.request.UpdateRequest
import jinho.han.userservice.dto.response.UserResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {
    @PostMapping("/signup")
    fun createUser(
        @RequestBody request: SignupRequest
    ): ResponseEntity<*> {
        val user: User = userService.createUser(request.email, request.password, request.name)
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(user))
    }

    @GetMapping("/me")
    fun getProfile(
        @RequestHeader("X-USER-ID") userId: Long
    ): ResponseEntity<*> {
        val user: User = userService.getUserById(userId)
        return ResponseEntity.ok(UserResponse.from(user))
    }

    @PutMapping("/me")
    fun updateProfile(
        @RequestHeader("X-USER-ID") userId: Long,
        @RequestBody request: UpdateRequest
    ): ResponseEntity<*> {
        val user: User = userService.updateUser(userId, request.name)
        return ResponseEntity.ok(UserResponse.from(user))
    }

    @PostMapping("/me/password")
    fun changePassword(
        @RequestHeader("X-USER-ID") userId: Long,
        @RequestBody request: PasswordChangeRequest
    ): ResponseEntity<*> {
        userService.changePassword(userId, request.currentPassword, request.newPassword)
        return ResponseEntity.ok().build<Any?>()
    }

    @GetMapping("/me/login-history")
    fun getLoginHistory(
        @RequestHeader("X-USER-ID") userId: Long
    ): ResponseEntity<List<UserLoginHistory>> {
        val history: List<UserLoginHistory> = userService.getLoginHistory(userId)
        return ResponseEntity.ok(history)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFound(exception: UserNotFoundException): ResponseEntity<String?> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body<String?>(exception.message)
    }

    @ExceptionHandler(DuplicateUserException::class)
    fun handleDuplicateUser(exception: DuplicateUserException): ResponseEntity<String?> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.message)
    }

    @ExceptionHandler(UnauthorizedAccessException::class)
    fun handleUnauthorizedAccess(exception: UnauthorizedAccessException): ResponseEntity<String?> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.message)
    }
}