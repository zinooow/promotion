package jinho.han.couponservice.adapter.web.dto

import jakarta.validation.constraints.NotBlank
import jinho.han.couponservice.application.v1.command.CouponPolicyCreateCommand
import java.time.LocalDateTime

data class CouponPolicyCreateRequest(

    @field:NotBlank
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