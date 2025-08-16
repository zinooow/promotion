package jinho.han.couponservice.application

import jinho.han.couponservice.adapter.persistance.CouponPolicyRepository
import jinho.han.couponservice.application.command.CouponPolicyCreateCommand
import jinho.han.couponservice.application.result.CouponPolicyResult
import jinho.han.couponservice.domain.CouponPolicy
import org.springframework.stereotype.Service

@Service
class CouponPolicyService(
    private val policyRepository: CouponPolicyRepository
) {

    fun createPolicy(policyCreateCommand: CouponPolicyCreateCommand): CouponPolicyResult {
        val createdPolicy = policyRepository.save(CouponPolicy.create(policyCreateCommand))
        return CouponPolicyResult.from(createdPolicy);
    }
}