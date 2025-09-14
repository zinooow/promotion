package jinho.han.couponservice.application.v1

import io.mockk.every
import io.mockk.mockkObject
import jinho.han.couponservice.adapter.persistance.CouponPolicyRepository
import jinho.han.couponservice.adapter.persistance.CouponRepository
import jinho.han.couponservice.application.v1.command.CouponIssueCommand
import jinho.han.couponservice.application.v1.command.CouponUseCommand
import jinho.han.couponservice.application.v1.exception.CouponNotFoundException
import jinho.han.couponservice.config.UserIdInterceptor
import jinho.han.couponservice.domain.Coupon
import jinho.han.couponservice.domain.CouponPolicy
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.test.util.ReflectionTestUtils
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
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
        ).apply { ReflectionTestUtils.setField(this, "id", 1L) }
    }

    @Test
    @DisplayName("쿠폰 발급 성공")
    fun issueCoupon_Success() {
        // Given
        val command = CouponIssueCommand (couponPolicyId = couponPolicy.id!!, userId = TEST_USER_ID)

        whenever(couponPolicyRepository.findById(any<Long>())).thenReturn(Optional.of(couponPolicy))
        whenever(couponRepository.countByCouponPolicyId(any<Long>())).thenReturn(0L)
        whenever(couponRepository.existsByUserIdAndCouponPolicyAndStatusNot(any<Long>(),any<CouponPolicy>(),any<Coupon.Status>())).thenReturn(false)
        whenever(couponRepository.save(any<Coupon>())).thenReturn(coupon)

        val response = couponService.issueCoupon(command)

        assertThat(response.id).isEqualTo(TEST_COUPON_ID)
        assertThat(response.userId).isEqualTo(TEST_USER_ID)
        verify(couponRepository).save(any())
    }

    @Test
    @DisplayName("쿠폰 사용 성공")
    fun useCoupon_Success() {
        val command = CouponUseCommand(couponId = coupon.id!!, orderId = TEST_ORDER_ID, orderAmount = 10000)

        mockkObject(UserIdInterceptor.Companion)
        every { UserIdInterceptor.getCurrentUserId() } returns TEST_USER_ID
        whenever(couponRepository.findById(any<Long>())).thenReturn(Optional.of(coupon))
        whenever(couponRepository.save(any<Coupon>())).thenReturn(coupon)

        couponService.useCoupon(command)

        assertThat (coupon.id).isEqualTo(TEST_COUPON_ID)
        assertThat (coupon.orderId).isEqualTo(TEST_ORDER_ID)
        assertThat (coupon.status).isEqualTo(Coupon.Status.USED)
    }

    @Test
    @DisplayName("쿠폰 사용 실패 - 쿠폰이 존재하지 않거나 권한이 없음")
    fun useCoupon_Fail_CouponNotFound() {
        val command = CouponUseCommand(couponId = coupon.id!!, orderId = TEST_ORDER_ID, orderAmount = 10000)

        mockkObject(UserIdInterceptor.Companion)
        every { UserIdInterceptor.getCurrentUserId() } returns TEST_USER_ID
        whenever(couponRepository.findById(any<Long>())).thenReturn(Optional.empty())

        assertThrows<CouponNotFoundException> { couponService.useCoupon(command) }
    }

    @Test
    @DisplayName("쿠폰 사용취소 성공")
    fun cancelCoupon_Success() {
        coupon.use(TEST_ORDER_ID, 10000)

        whenever(couponRepository.findById(any<Long>())).thenReturn(Optional.of(coupon))
        whenever(couponRepository.save(any<Coupon>())).thenReturn(coupon)

        couponService.cancelCoupon(TEST_COUPON_ID)

        assertThat (coupon.id).isEqualTo(TEST_COUPON_ID)
        assertThat (coupon.orderId).isNull()
        assertThat (coupon.status).isEqualTo(Coupon.Status.CANCELLED)
    }

    @Test
    @DisplayName("쿠폰 사용취소 실패")
    fun cancelCoupon_Fail() {
        whenever(couponRepository.findById(any<Long>())).thenReturn(Optional.empty())

        assertThrows<CouponNotFoundException>{couponService.cancelCoupon(TEST_COUPON_ID)}
    }

}