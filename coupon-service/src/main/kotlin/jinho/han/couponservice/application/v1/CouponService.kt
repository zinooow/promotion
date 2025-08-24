package jinho.han.couponservice.application.v1

import jinho.han.couponservice.adapter.persistance.CouponPolicyRepository
import jinho.han.couponservice.adapter.persistance.CouponRepository
import jinho.han.couponservice.application.v1.command.CouponIssueCommand
import jinho.han.couponservice.application.v1.command.CouponUseCommand
import jinho.han.couponservice.application.v1.exception.CouponAlreadyIssuedException
import jinho.han.couponservice.application.v1.exception.CouponNotFoundException
import jinho.han.couponservice.application.v1.exception.CouponPolicyNotFoundException
import jinho.han.couponservice.application.v1.result.CouponResult
import jinho.han.couponservice.config.UserIdInterceptor.Companion.getCurrentUserId
import jinho.han.couponservice.domain.Coupon
import jinho.han.couponservice.domain.exception.CouponExpiredException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
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
        val couponPolicy = couponPolicyRepository.findById(command.couponPolicyId).getOrNull()
            ?: throw CouponPolicyNotFoundException()

        require(couponPolicy.totalQuantity >couponRepository.countByCouponPolicyId(command.couponPolicyId)) {  }

        couponPolicy.validate()

        // 해당 유저 쿠폰 발급이력 확인
        couponRepository.findByUserIdAndCouponPolicyAndStatusNot(command.userId, couponPolicy, Coupon.Status.CANCELLED)
            ?.let{ throw CouponAlreadyIssuedException(it.id!!) }

        // 발급 후 저장
        return couponRepository.save(Coupon.issue(command.userId, couponPolicy))
    }

    @Transactional
    fun useCoupon(command: CouponUseCommand): Int {
        val userId = getCurrentUserId()
        val coupon = couponRepository.findById(command.couponId).getOrNull()
            ?: throw CouponNotFoundException(command.couponId)
        require(userId == coupon.userId) { throw IllegalStateException("쿠폰의 유저와 요청을 보낸 유저가 일치하지 않습니다.") }
        val result = coupon.use(orderId = command.orderId, amount = command.orderAmount)
        couponRepository.save(coupon)
        return result
    }

    @Transactional
    fun cancelCoupon(couponId: Long, reIssue: Boolean): CouponResult? {
        val coupon = couponRepository.findById(couponId).getOrNull()
            ?: throw CouponNotFoundException(couponId)
        coupon.cancel()
        couponRepository.save(coupon)
        // 발급 취소 후 만료 전이면 재발급
        if(reIssue) {
            try {
                val reIssuedCoupon = coupon.reIssue()
                couponRepository.save(reIssuedCoupon)
                return CouponResult.from(reIssuedCoupon)
            } catch(e: CouponExpiredException) {
                return null
            }
        } else {
            return null
        }
    }

    @Transactional(readOnly = true)
    fun getCouponByUserId(userId: Long, page: Int, pageSize: Int): List<CouponResult> =
        couponRepository.findByUserIdAndStatusOrderByIssuedAtDesc(
            userId,
            Coupon.Status.AVAILABLE,
            PageRequest.of(
                page,
                pageSize,
                Sort.by("issuedAt").descending()
            )
        ).content.map(CouponResult::from)

}