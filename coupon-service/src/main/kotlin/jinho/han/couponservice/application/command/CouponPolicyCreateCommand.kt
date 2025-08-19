package jinho.han.couponservice.application.command

import jinho.han.couponservice.domain.DiscountType
import java.time.LocalDateTime

data class CouponPolicyCreateCommand(
    val title: String,
    val description: String,
    val totalQuantity: Int,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val discountType: DiscountType,
    val discountValue: Int,
    val minOrderAmount: Int,
    val maxDiscountAmount: Int
)