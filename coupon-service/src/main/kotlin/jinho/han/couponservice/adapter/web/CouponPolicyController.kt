package jinho.han.couponservice.adapter.web

import jinho.han.couponservice.adapter.web.dto.ApiResponse
import jinho.han.couponservice.adapter.web.dto.CouponPolicyCreateRequest
import jinho.han.couponservice.application.CouponPolicyService
import jinho.han.couponservice.application.result.CouponPolicyResult
import jinho.han.couponservice.domain.CouponPolicy
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/coupon-policies")
class CouponPolicyController(
    private val couponPolicyService: CouponPolicyService,
) {

    /*
    * Method: POST
    * Path: /api/v1/coupon-policies
    * Description: 새로운 쿠폰 정책을 생성합니다.
    */
    @PostMapping
    fun createCouponPolicy(
        @RequestBody policyCreateRequest: CouponPolicyCreateRequest
    ): ApiResponse<CouponPolicyResult> =
        ApiResponse(
            code = HttpStatus.CREATED.value(),
            message = "Coupon Policy Create Success",
            data = couponPolicyService.createPolicy(policyCreateRequest.toCommand())
        )

    /*
    * Method: GET
    * Path: /api/v1/coupon-policies/{id}
    * Description: 특정 쿠폰 정책의 상세 정보를 조회합니다.
    */
    @GetMapping("/{id}")
    fun getCouponPolicy(@PathVariable("id") id: Long): ResponseEntity<ApiResponse<CouponPolicyResult?>> =
        couponPolicyService.getCouponPolicy(id)
            ?.let { ResponseEntity.ok(
                ApiResponse(code = HttpStatus.OK.value(), message = "Coupon Policy Get Success", data = it )
            )}
            ?: ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse(code = HttpStatus.NOT_FOUND.value(), message = "Coupon Policy Not Found", data = null)
            )


    /*
    * Method: GET
    * Path: /api/v1/coupon-policies
    * Description: 쿠폰 정책 목록을 조회합니다.
    * Parameters:
    *   status (optional): 쿠폰정책 상태
    *   issuedAt (optional): 발행일
    *   page (optional): 페이지 번호 (default: 0)
    *   size (optional): 페이지 크기 (default: 10)
    */
    @GetMapping
    fun getCouponPolicyList(): List<CouponPolicyResult> = couponPolicyService.getCouponPolicyList()


}

