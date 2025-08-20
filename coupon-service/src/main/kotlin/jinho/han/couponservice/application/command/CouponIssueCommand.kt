package jinho.han.couponservice.application.command

data class CouponIssueCommand(
    val couponPolicyId: Long,
    val userId: Long
) {
}