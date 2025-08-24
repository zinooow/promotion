package jinho.han.couponservice.application.v1

import jinho.han.couponservice.adapter.persistance.CouponPolicyRepository
import jinho.han.couponservice.adapter.persistance.CouponRepository
import jinho.han.couponservice.application.v1.command.CouponIssueCommand
import jinho.han.couponservice.domain.Coupon
import jinho.han.couponservice.domain.CouponPolicy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.springframework.test.util.ReflectionTestUtils
import java.time.LocalDateTime
import java.util.Optional

class CouponServiceTest {

    @InjectMocks
    lateinit var couponService: CouponService

    @Mock
    lateinit var couponRepository: CouponRepository

    @Mock
    lateinit var couponPolicyRepository: CouponPolicyRepository

    lateinit var couponPolicy: CouponPolicy
    lateinit var coupon: Coupon
    companion object {
        private val TEST_USER_ID: Long = 1L
        private val TEST_COUPON_ID: Long = 1L
        private val TEST_ORDER_ID: Long = 1L
    }

    @BeforeEach
    fun setUp() {
        couponPolicy = CouponPolicy.create(
            title = "쿠폰발급테스트정책",
            description = "쿠폰발급테스트용정책",
            totalQuantity = 100,
            startTime = LocalDateTime.now().minusDays(1),
            endTime = LocalDateTime.now().plusDays(1),
            discountType = CouponPolicy.DiscountType.FIXED_AMOUNT,
            discountValue = 1000,
            minOrderAmount = 10000,
            maxDiscountAmount = 1000
        ).apply { ReflectionTestUtils.setField(this, "id", 1L) }

        coupon = Coupon.issue(
            userId = TEST_USER_ID,
            couponPolicy = couponPolicy,
        )
    }

    @Test
    @DisplayName("쿠폰 발급 성공")
    fun issueCouponSuccess() {
        val command = CouponIssueCommand (couponPolicyId = couponPolicy.id!!, userId = TEST_USER_ID)

        `when`(couponPolicyRepository.findById(any())).thenReturn(Optional.of(couponPolicy))
        `when`(couponRepository.countByCouponPolicyId(any())).thenReturn(0L)
        `when`(couponRepository.existsByUserIdAndCouponPolicyAndStatusNot(any(),any(),any())).thenReturn(false)
        `when`(couponRepository.save(any())).thenReturn(coupon)

    }

}