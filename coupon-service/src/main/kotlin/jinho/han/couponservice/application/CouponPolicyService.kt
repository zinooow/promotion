package jinho.han.couponservice.application

import jinho.han.couponservice.adapter.persistance.CouponPolicyRepository
import jinho.han.couponservice.application.command.CouponPolicyCreateCommand
import jinho.han.couponservice.application.result.CouponPolicyResult
import jinho.han.couponservice.domain.CouponPolicy
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class CouponPolicyService(
    private val policyRepository: CouponPolicyRepository
) {

    fun createPolicy(policyCreateCommand: CouponPolicyCreateCommand): CouponPolicyResult =
        policyRepository.save(CouponPolicy.create(
            title = policyCreateCommand.title,
            description = policyCreateCommand.description,
            totalQuantity = policyCreateCommand.totalQuantity,
            startTime = policyCreateCommand.startTime,
            endTime = policyCreateCommand.endTime,
            discountType = policyCreateCommand.discountType,
            discountValue = policyCreateCommand.discountValue,
            minOrderAmount = policyCreateCommand.minOrderAmount,
            maxDiscountAmount = policyCreateCommand.maxDiscountAmount
        )).let(CouponPolicyResult::from)

    fun getCouponPolicy(id: Long): CouponPolicyResult? =
        policyRepository.findById(id).getOrNull()
            ?.let { CouponPolicyResult.from(it) }


    fun getCouponPolicyList(): List<CouponPolicyResult> =
        policyRepository.findAll()
            .map ( CouponPolicyResult::from )
}