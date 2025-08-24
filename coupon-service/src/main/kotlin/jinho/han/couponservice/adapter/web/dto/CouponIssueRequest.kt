package jinho.han.couponservice.adapter.web.dto

import jinho.han.couponservice.application.v1.command.CouponIssueCommand

data class CouponIssueRequest (
    val couponPolicyId: Long,
    val userId: Long
) {
    fun toCommand(): CouponIssueCommand = CouponIssueCommand(couponPolicyId, userId)
}
