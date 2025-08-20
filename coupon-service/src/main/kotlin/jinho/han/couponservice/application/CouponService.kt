package jinho.han.couponservice.application

import jinho.han.couponservice.adapter.persistance.CouponPolicyRepository
import jinho.han.couponservice.adapter.persistance.CouponRepository
import jinho.han.couponservice.application.command.CouponIssueCommand
import jinho.han.couponservice.application.exception.InvalidCouponPolicy
import jinho.han.couponservice.domain.CouponPolicy
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class CouponService(
    private val couponRepository: CouponRepository,
    private val couponPolicyRepository: CouponPolicyRepository,
) {

    fun issueCoupon(command: CouponIssueCommand) {
        // 쿠폰 검증
        val couponPolicy = couponPolicyRepository.findById(command.couponPolicyId).getOrNull()?: throw InvalidCouponPolicy("Coupon policy not found")
        couponPolicy.validate()

        // 해당 유저 쿠폰 발급이력 확인
        couponRepository.findByUserIdAndCouponPolicy(command.couponPolicyId, couponPolicy)
    }
}