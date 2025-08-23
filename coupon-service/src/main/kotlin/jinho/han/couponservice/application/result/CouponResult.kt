package jinho.han.couponservice.application.result

import jinho.han.couponservice.domain.Coupon
import java.time.LocalDateTime

data class CouponResult(
    val id: Long,
    val couponPolicy: CouponPolicyResult,
    val userId: Long,
    val status: String,
    val orderId: Long? = null,
    val expirationTime: LocalDateTime,
    val issuedAt: LocalDateTime
) {
    companion object {
        fun from(coupon: Coupon): CouponResult {
            return CouponResult(
                id = coupon.id!!,
                couponPolicy = CouponPolicyResult.from(coupon.couponPolicy),
                userId = coupon.userId,
                status = coupon.status.name,
                orderId = coupon.orderId,
                expirationTime = coupon.expirationTime,
                issuedAt = coupon.issuedAt,
            )
        }
    }
}


