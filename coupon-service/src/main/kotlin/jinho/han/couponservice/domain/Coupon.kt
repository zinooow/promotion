package jinho.han.couponservice.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "coupon")
class Coupon (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_policy_id", nullable = false)
    val couponPolicy: CouponPolicy,

    @Column(nullable = false)
    val userId: Long,

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    val status: CouponStatus,
    val orderId: Long? = null,

    @Column(nullable = false)
    val expirationTime: LocalDateTime,

    @Column(nullable = false)
    val issuedAt: LocalDateTime,
)

