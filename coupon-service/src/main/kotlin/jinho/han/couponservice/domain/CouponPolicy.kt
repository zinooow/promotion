package jinho.han.couponservice.domain

import jakarta.persistence.*
import jinho.han.couponservice.domain.exception.InvalidCouponPeriodException
import java.time.LocalDateTime

@Entity
@Table(name = "coupon_policy")
class CouponPolicy
    private constructor(
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
        val maxDiscountAmount: Int,

        @Column
        val createdAt: LocalDateTime = LocalDateTime.now(),
    )
{
    enum class DiscountType {
        FIXED_AMOUNT,
        PERCENTAGE
    }
    companion object{
        fun create(
            title: String,
            description: String,
            totalQuantity: Int,
            startTime: LocalDateTime,
            endTime: LocalDateTime,
            discountType: DiscountType,
            discountValue: Int,
            minOrderAmount: Int,
            maxDiscountAmount: Int
        ) = CouponPolicy(
            id = null,
            title = title,
            description = description,
            totalQuantity = totalQuantity,
            startTime = startTime,
            endTime = endTime,
            discountType = discountType,
            discountValue = discountValue,
            minOrderAmount = minOrderAmount,
            maxDiscountAmount = maxDiscountAmount
        )
    }

    fun validate() {
        // 발행시간 검증 -> IllegalArgumentException
        require(this.startTime.isBefore(LocalDateTime.now()) || this.endTime.isAfter(LocalDateTime.now())) { throw InvalidCouponPeriodException("쿠폰 발급 가능한 기간이 아닙니다.") }
    }
}

