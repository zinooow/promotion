package jinho.han.couponservice.application

import jinho.han.couponservice.adapter.persistance.CouponPolicyRepository
import jinho.han.couponservice.adapter.persistance.CouponRepository
import jinho.han.couponservice.application.command.CouponIssueCommand
import jinho.han.couponservice.application.command.CouponUseCommand
import jinho.han.couponservice.application.exception.CouponNotFoundException
import jinho.han.couponservice.application.exception.ExpiredCouponException
import jinho.han.couponservice.application.exception.InvalidCouponPolicyException
import jinho.han.couponservice.application.result.CouponResult
import jinho.han.couponservice.domain.Coupon
import jinho.han.couponservice.domain.CouponStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class CouponService(
    private val couponRepository: CouponRepository,
    private val couponPolicyRepository: CouponPolicyRepository,
) {

    @Transactional
    fun issueCoupon(command: CouponIssueCommand): Coupon {
        // 쿠폰 검증 (수량, 날짜)
        val couponPolicy = couponPolicyRepository.findById(command.couponPolicyId).getOrNull()?: throw InvalidCouponPolicyException("쿠폰 정책을 찾을 수 없습니다.")
        require(couponPolicy.totalQuantity >couponRepository.countByCouponPolicyId(command.couponPolicyId)) {}
        couponPolicy.validate()

        // 해당 유저 쿠폰 발급이력 확인
        couponRepository.findByUserIdAndCouponPolicyAndStatusNot(command.userId, couponPolicy, CouponStatus.CANCELLED)?.let{ throw InvalidCouponPolicyException("해당 유저는 이미 쿠폰을 발급 받았습니다. userId:${command.userId}") }
        // 발급 후 저장
        return couponRepository.save(Coupon.issue(command.userId, couponPolicy))
    }

    @Transactional
    fun useCoupon(command: CouponUseCommand): Int {
        val coupon = couponRepository.findById(command.couponId).getOrNull()?: throw CouponNotFoundException("해당 쿠폰을 찾을 수 없습니다.")
        val result = coupon.use(orderId = command.orderId, amount = command.orderAmount)
        couponRepository.save(coupon)
        return result
    }

    @Transactional
    fun cancelCoupon(couponId: Long, reIssue: Boolean): CouponResult? {
        val coupon = couponRepository.findById(couponId).getOrNull()?: throw CouponNotFoundException("해당 쿠폰을 찾을 수 없습니다.")
        coupon.cancel()
        couponRepository.save(coupon)
        // 발급 취소 후 만료 전이면 재발급
        if(reIssue) {
            try {
                val reIssuedCoupon = coupon.reIssue()
                couponRepository.save(reIssuedCoupon)
                return CouponResult.from(reIssuedCoupon)
            } catch(e: ExpiredCouponException) {
                return null
            }
        } else {
            return null
        }
    }

    @Transactional(readOnly = true)
    fun getCouponByUserId(userId: Long): List<CouponResult> =
        couponRepository.findByUserId(userId)
            .map(CouponResult::from)

}