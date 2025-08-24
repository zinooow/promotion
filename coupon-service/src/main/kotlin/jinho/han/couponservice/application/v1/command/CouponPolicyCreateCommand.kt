package jinho.han.couponservice.application.v1.command

import java.time.LocalDateTime

data class CouponPolicyCreateCommand(
    val title: String,
    val description: String,
    val totalQuantity: Int,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val discountType: String,
    val discountValue: Int,
    val minOrderAmount: Int,
    val maxDiscountAmount: Int
)