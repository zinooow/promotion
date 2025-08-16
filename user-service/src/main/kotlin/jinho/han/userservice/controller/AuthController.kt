package jinho.han.userservice.controller

import io.jsonwebtoken.Claims
import jinho.han.userservice.dto.response.LoginResponse
import jinho.han.userservice.dto.request.TokenRequest
import jinho.han.userservice.dto.response.TokenValidateResponse
import jinho.han.userservice.dto.response.UserResponse
import jinho.han.userservice.application.JwtService
import jinho.han.userservice.application.UserService
import jinho.han.userservice.dto.request.LoginRequest
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
        val user = userService.authenticate(request.email, request.password)
        val token = jwtService.generateJwtToken(user)
        return ResponseEntity.ok(
            LoginResponse(
                user = UserResponse.from(user),
                token = token
            )
        )
    }

    @PostMapping("/validate-token")
    fun validateToken(
        @RequestBody tokenValidateRequest: TokenRequest
    ) : ResponseEntity<TokenValidateResponse> {
        val claims: Claims = jwtService.validateJwtToken(tokenValidateRequest.token)
        return ResponseEntity.ok(TokenValidateResponse(
            email = claims.subject,
            name = claims["name"].toString(),
            valid = true,
            role = claims["role"].toString()
        ))
    }

    @PostMapping("/refresh-token")
    fun refreshToken(@RequestBody request: TokenRequest) : ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf("token" to jwtService.refreshJwtToken(request.token)))
    }


}