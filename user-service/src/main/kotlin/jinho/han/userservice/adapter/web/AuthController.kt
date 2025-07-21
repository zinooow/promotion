package jinho.han.userservice.adapter.web

import jinho.han.userservice.adapter.web.dto.LoginResponse
import jinho.han.userservice.adapter.web.dto.UserResponse
import jinho.han.userservice.application.JwtService
import jinho.han.userservice.application.UserService
import jinho.han.userservice.application.dto.LoginRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class AuthController(
    private val userService: UserService,
    private val jwtService: JwtService
) {
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest) : ResponseEntity<LoginResponse> {
        val user = userService.authenticate(request)
        val token = jwtService.generateJwtToken(user)
        return ResponseEntity.ok(
            LoginResponse(
                user = UserResponse.from(user),
                token = token
            )
        )
    }


}