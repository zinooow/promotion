package jinho.han.couponservice.adapter.web

import jinho.han.couponservice.adapter.web.dto.ApiResponse
import jinho.han.couponservice.adapter.web.dto.CouponPolicyCreateRequest
import jinho.han.couponservice.application.CouponPolicyService
import jinho.han.couponservice.application.result.CouponPolicyResult
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/coupon-policies")
class CouponPolicyController(
    private val couponPolicyService: CouponPolicyService,
) {

    @PostMapping
    fun createCouponPolicy(
        @RequestBody policyCreateRequest: CouponPolicyCreateRequest
    ): ApiResponse<CouponPolicyResult> {

        return ApiResponse(
            code = HttpStatus.CREATED.value(),
            message = "Coupon Policy Create Success",
            data = couponPolicyService.createPolicy(policyCreateRequest.toCommand())
        )
    }


}

