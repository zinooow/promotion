package jinho.han.couponservice.adapter.web.dto

import jinho.han.couponservice.application.command.CouponPolicyCreateCommand
import jinho.han.couponservice.domain.DiscountType
import java.time.LocalDateTime

data class CouponPolicyCreateRequest(
    val title: String,
    val description: String,
    val totalQuantity: Int,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val discountType: DiscountType,
    val discountValue: Int,
    val minOrderAmount: Int,
    val maxDiscountAmount: Int
) {
    fun toCommand(): CouponPolicyCreateCommand {
        return CouponPolicyCreateCommand(
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
}