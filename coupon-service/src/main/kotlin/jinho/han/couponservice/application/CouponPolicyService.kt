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
        val createdPolicy = policyRepository.save(CouponPolicy.create(
            title = policyCreateCommand.title,
            description = policyCreateCommand.description,
            totalQuantity = policyCreateCommand.totalQuantity,
            startTime = policyCreateCommand.startTime,
            endTime = policyCreateCommand.endTime,
            discountType = policyCreateCommand.discountType,
            discountValue = policyCreateCommand.discountValue,
            minOrderAmount = policyCreateCommand.minOrderAmount,
            maxDiscountAmount = policyCreateCommand.maxDiscountAmount
        ))

        return CouponPolicyResult.from(createdPolicy);
    }
}