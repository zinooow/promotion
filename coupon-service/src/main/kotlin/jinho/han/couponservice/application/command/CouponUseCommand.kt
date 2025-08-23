package jinho.han.couponservice.application.command

data class CouponUseCommand(
    val couponId: Long,
    val orderId: Long,
    val orderAmount: Int
)