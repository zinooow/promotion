package jinho.han.couponservice.domain

import jakarta.persistence.*
import jinho.han.couponservice.application.command.CouponPolicyCreateCommand
import java.time.LocalDateTime

@Entity
@Table(name = "coupon_policy")
class CouponPolicy (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val description: String,

    @Column(nullable = false)
    val totalQuantity: Int,

    @Column(nullable = false)
    val startTime: LocalDateTime,

    @Column(nullable = false)
    val endTime: LocalDateTime,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val discountType: DiscountType,

    @Column(nullable = false)
    val discountValue: Int,

    @Column(nullable = false)
    val minOrderAmount: Int,

    @Column(nullable = false)
    val maxDiscountAmount: Int
) {
    companion object{
        fun create(policyCreateCommand: CouponPolicyCreateCommand) = CouponPolicy(
            id = null,
            title = policyCreateCommand.title,
            description = policyCreateCommand.description,
            totalQuantity = policyCreateCommand.totalQuantity,
            startTime = policyCreateCommand.startTime,
            endTime = policyCreateCommand.endTime,
            discountType = policyCreateCommand.discountType,
            discountValue = policyCreateCommand.discountValue,
            minOrderAmount = policyCreateCommand.minOrderAmount,
            maxDiscountAmount = policyCreateCommand.maxDiscountAmount
        )
    }
}

