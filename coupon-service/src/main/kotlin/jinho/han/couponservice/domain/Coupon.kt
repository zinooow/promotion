package jinho.han.couponservice.domain

import jakarta.persistence.*
import jinho.han.couponservice.application.exception.ExpiredCouponException
import jinho.han.couponservice.application.exception.InvalidCouponException
import java.time.LocalDateTime
import kotlin.math.roundToInt

@Entity
@Table(name = "coupon")
class Coupon (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_policy_id", nullable = false)
    val couponPolicy: CouponPolicy,

    @Column(nullable = false)
    val userId: Long,

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    var status: CouponStatus,
    var orderId: Long? = null,

    // TODO
    //  추후 쿠폰소멸 정책 생성 필요
    @Column(nullable = false)
    val expirationTime: LocalDateTime,

    @Column(nullable = false)
    val issuedAt: LocalDateTime,
) {
    companion object{
        fun issue(userId: Long, couponPolicy: CouponPolicy): Coupon {
            return Coupon(
                userId = userId,
                couponPolicy = couponPolicy,
                status = CouponStatus.AVAILABLE,
                issuedAt = LocalDateTime.now(),
                expirationTime = LocalDateTime.now().plusDays(7),
            )
        }
    }
    fun use(orderId: Long, amount: Int): Int {
        require(this.expirationTime.isBefore(LocalDateTime.now())) { "쿠폰이 만료되었습니다." }
        require(this.couponPolicy.minOrderAmount <= amount) { "쿠폰의 최소 사용금액: ${this.couponPolicy.minOrderAmount} 보다 높아야 합니다." }
        require(this.status == CouponStatus.AVAILABLE)
        require(this.orderId == null)
        this.status = CouponStatus.USED
        this.orderId = orderId

        return calculateDiscount(amount)
    }

    fun calculateDiscount(amount: Int): Int {
        return if(this.couponPolicy.discountType == DiscountType.PERCENTAGE)
        { amount * (1-this.couponPolicy.discountValue / 100.0).roundToInt()}
        else { amount-this.couponPolicy.discountValue.coerceAtLeast(0) }
    }

    fun cancel() {
        require(this.status == CouponStatus.USED)
        require(this.orderId == null) { "coupon order id is null" }
        this.status = CouponStatus.CANCELLED
    }

    fun reIssue(): Coupon {
        require(this.status == CouponStatus.CANCELLED) { throw InvalidCouponException("취소상태가 아닌 쿠폰은 재발급 불가능합니다.") }
        require(this.expirationTime.isAfter(LocalDateTime.now())) { throw ExpiredCouponException("this coupon cant reissue") }
        return Coupon(
            userId = this.userId,
            couponPolicy = this.couponPolicy,
            status = CouponStatus.AVAILABLE,
            issuedAt = this.issuedAt,
            expirationTime = this.expirationTime,
        )
    }
}

