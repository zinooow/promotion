package jinho.han.userservice.application

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jinho.han.userservice.domain.User
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.*

@Service
class JwtService(
    @Value("\${jwt.secret}")
    private val secretKey: String
) {


    companion object{
        val log = LoggerFactory.getLogger(JwtService::class.java)
    }

    fun generateJwtToken(user: User): String {
        val currentTimeMills = System.currentTimeMillis()
        return Jwts.builder()
            .subject(user.email)
            .claim("role", "USER")
            .issuedAt(Date(currentTimeMills))
            .expiration(Date(currentTimeMills+3600000))
            .signWith(Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8)))
            .compact()
    }

    fun validateJwtToken(token: String): Claims {
        try {
            return parseJwtClaims(token)
        } catch(e: ExpiredJwtException){
            log.error("JWT Token Expired Error : ${e.message}")
            throw e
        } catch (e: JwtException) {
            log.error("JWT Token Validation Error : ${e.message}")
            throw e
        }
    }

    private fun parseJwtClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8)))
            .build()
            .parseSignedClaims(token)
            .payload
    }

    fun refreshJwtToken(token: String): String {
        val claims = parseJwtClaims(token)
        val currentTimeMills = System.currentTimeMillis()

        return Jwts.builder()
            .subject(claims.subject)
            .claims(claims)
            .issuedAt(Date(currentTimeMills))
            .expiration(Date(currentTimeMills+3600000))
            .signWith(Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8)))
            .compact()

    }

}