package jinho.han.couponservice.application.v1.result

import jinho.han.couponservice.domain.CouponPolicy
import java.time.LocalDateTime
import kotlin.Int

data class CouponPolicyResult(
    val id: Long,
    val title: String,
    val description: String,
    val totalQuantity: Int,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val discountType: String,
    val discountValue: Int,
    val minOrderAmount: Int,
    val maxDiscountAmount: Int
) {
    companion object{
        fun from(couponPolicy: CouponPolicy): CouponPolicyResult = CouponPolicyResult(
            id = couponPolicy.id!!,
            title = couponPolicy.title,
            description = couponPolicy.description,
            totalQuantity = couponPolicy.totalQuantity,
            startTime = couponPolicy.startTime,
            endTime = couponPolicy.endTime,
            discountType = couponPolicy.discountType.name,
            discountValue = couponPolicy.discountValue,
            minOrderAmount = couponPolicy.minOrderAmount,
            maxDiscountAmount = couponPolicy.maxDiscountAmount
        )
    }
}