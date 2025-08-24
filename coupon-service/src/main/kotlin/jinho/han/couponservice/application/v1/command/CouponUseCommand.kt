package jinho.han.couponservice.application.v1.command

data class CouponUseCommand(
    val couponId: Long,
    val orderId: Long,
    val orderAmount: Int
)