package jinho.han.userservice.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "login_history")
class UserLoginHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @Column(name = "login_time", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    var loginTime: LocalDateTime,

    @Column(name = "ip_address", nullable = false)
    var ipaddress: String



    ) {


}