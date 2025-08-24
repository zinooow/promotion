package jinho.han.couponservice.application.v1.command

data class CouponIssueCommand(
    val couponPolicyId: Long,
    val userId: Long
) {
}