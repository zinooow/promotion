package jinho.han.couponservice.adapter.web

import jakarta.validation.Valid
import jinho.han.couponservice.adapter.web.dto.ApiResponse
import jinho.han.couponservice.adapter.web.dto.CouponIssueRequest
import jinho.han.couponservice.application.CouponService
import jinho.han.couponservice.application.command.CouponUseCommand
import jinho.han.couponservice.application.result.CouponResult
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/coupons")
class CouponController(
    private val couponService: CouponService,
) {
    /*
     * Method: POST
     * Path: /api/v1/coupons/issue
     * Description: 사용자에게 쿠폰을 발행합니다.
     */
    @PostMapping("/issue")
    fun issueCoupon(@RequestBody @Valid issueRequest: CouponIssueRequest): ApiResponse<CouponResult> {
        val issuedCoupon = couponService.issueCoupon(issueRequest.toCommand())
        return ApiResponse(
            code = 200,
            message = "쿠폰 발급에 성공했습니다. userId: ${issueRequest.userId}, couponId: ${issuedCoupon.id!!}",
            data = CouponResult.from(issuedCoupon)
        )
    }

    /*
     * Method: POST
     * Path: /api/v1/coupons/{id}/use
     * Description: 쿠폰을 사용 처리합니다.
     */
    @PostMapping("/{id}/use")
    fun useCoupon(
        @PathVariable("id") couponId: Long,
        orderId: Long,
        orderAmount: Int
    ):ApiResponse<Int> {
        val command = CouponUseCommand(couponId = couponId, orderId = orderId, orderAmount = orderAmount)
        val discountApplyAmount = couponService.useCoupon(command)
        return ApiResponse(
            code = 200,
            message = "쿠폰의 사용이 완료되었습니다. couponId:${couponId}",
            data = discountApplyAmount
        )
    }

    /*
     * Method: POST
     * Path: /api/v1/coupons/{id}/cancel
     * Description: 사용된 쿠폰을 취소 처리합니다.
     */
    @PostMapping("/{id}/cancel")
    fun cancelCoupon(@PathVariable("id") couponId: Long, @RequestParam(required = false) reIssue: Boolean): ApiResponse<CouponResult?> {
        val reIssuedCoupon = couponService.cancelCoupon(couponId, reIssue)
        val message = if(!reIssue)
            {"couponId:${couponId} 쿠폰의 취소가 완료되었습니다"}
            else {
                if(reIssuedCoupon == null){"쿠폰의 취소가 완료되었습니다. 해당 쿠폰은 만료된 쿠폰이므로, 재발급이 불가합니다."}
                else {"쿠폰의 취소가 완료되었습니다. 해당 쿠폰의 재발급이 완료되었습니다."}
            }
        return ApiResponse(
            code = 200,
            message = message,
            data = reIssuedCoupon
        )
    }


    /*
     * Method: GET
     *  Path: /api/v1/coupons/user/{userId}
     *  Description: 특정 사용자의 쿠폰 목록을 조회합니다.
     *  Parameters:
     *    status (optional): 쿠폰 상태 필터
     *    page (optional): 페이지 번호 (default: 0)
     *    size (optional): 페이지 크기 (default: 10)
     */
    @GetMapping("/user/{userId}")
    fun getCoupons(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "page_size", defaultValue = "10") pageSize: Int,
        @PathVariable("userId") userId: Long
    ): ApiResponse<List<CouponResult>> {
        val couponList = couponService.getCouponByUserId(userId)
        return ApiResponse(
            code = if(couponList.isEmpty()) {HttpStatus.NOT_FOUND.value()} else {HttpStatus.OK.value()},
            message = if(couponList.isEmpty()) {"해당 조건에 맞는 결과가 없습니다."} else {"조회에 성공했습니다."},
            data = couponList
        )
    }

}