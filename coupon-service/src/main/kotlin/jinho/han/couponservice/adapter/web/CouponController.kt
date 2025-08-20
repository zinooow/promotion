package jinho.han.couponservice.adapter.web

import jakarta.validation.Valid
import jinho.han.couponservice.adapter.web.dto.CouponIssueRequest
import jinho.han.couponservice.application.CouponService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/coupons")
class CouponController(
    private val couponService: CouponService,
) {
    @PostMapping("/issue")
    fun issueCoupon(@RequestBody @Valid issueRequest: CouponIssueRequest) {
        couponService.issueCoupon(issueRequest.toCommand())
    }

}