package jinho.han.couponservice.application.v1

import jinho.han.couponservice.adapter.persistance.CouponPolicyRepository
import jinho.han.couponservice.application.v1.command.CouponPolicyCreateCommand
import jinho.han.couponservice.application.v1.result.CouponPolicyResult
import jinho.han.couponservice.domain.CouponPolicy
import jinho.han.couponservice.util.enumValueOfSafe
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class CouponPolicyService(
    private val policyRepository: CouponPolicyRepository
) {

    @Transactional
    fun createPolicy(policyCreateCommand: CouponPolicyCreateCommand): CouponPolicyResult {
        // TODO : validate

        return policyRepository.save(CouponPolicy.create(
            title = policyCreateCommand.title,
            description = policyCreateCommand.description,
            totalQuantity = policyCreateCommand.totalQuantity,
            startTime = policyCreateCommand.startTime,
            endTime = policyCreateCommand.endTime,
            discountType = enumValueOfSafe<CouponPolicy.DiscountType>(policyCreateCommand.discountType),
            discountValue = policyCreateCommand.discountValue,
            minOrderAmount = policyCreateCommand.minOrderAmount,
            maxDiscountAmount = policyCreateCommand.maxDiscountAmount
        )).let(CouponPolicyResult::from)
    }


    @Transactional(readOnly = true)
    fun getCouponPolicyById(id: Long): CouponPolicyResult? =
        policyRepository.findById(id).getOrNull()
            ?.let { CouponPolicyResult.from(it) }

    @Transactional(readOnly = true)
    fun getCouponPolicyList(page: Int, pageSize: Int): List<CouponPolicyResult> =
        policyRepository.findAll(PageRequest.of(page, pageSize, Sort.by("createdAt").descending())).content
            .map (CouponPolicyResult::from)
}