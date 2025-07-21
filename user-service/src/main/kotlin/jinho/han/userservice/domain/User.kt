package jinho.han.userservice.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jinho.han.userservice.application.UserCreateRequest
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "name", nullable = false, length = 100)
    var name: String,

    @Column(name = "email", nullable = false, length = 150)
    var email: String,

    @JsonIgnore
    @Column(name = "password_hash", nullable = false, length = 255)
    var passwordHash: String,

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @UpdateTimestamp
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    companion object{
        fun create(userCreateRequest: UserCreateRequest, passwordEncoder: PasswordEncoder): User = User(
            name = userCreateRequest.name,
            email = userCreateRequest.email,
            passwordHash = passwordEncoder.encode(userCreateRequest.password)
        )
    }
}
