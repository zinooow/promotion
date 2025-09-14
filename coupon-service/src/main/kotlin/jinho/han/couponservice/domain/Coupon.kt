package jinho.han.couponservice.domain

import jakarta.persistence.*
import jinho.han.couponservice.domain.exception.CouponAlreadyUsedException
import jinho.han.couponservice.domain.exception.CouponExpiredException
import jinho.han.couponservice.domain.exception.CouponNotAvailableException
import jinho.han.couponservice.domain.exception.InvalidCouponException
import java.time.LocalDateTime
import kotlin.math.roundToInt

@Entity
@Table(name = "coupon")
class Coupon
    private constructor(
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
        var status: Status,
        var orderId: Long? = null,
        var usedAt: LocalDateTime? = null,

        // TODO
        //  추후 쿠폰소멸 정책 생성 필요
        @Column(nullable = false)
        val expirationTime: LocalDateTime,

        @Column(nullable = false)
        val issuedAt: LocalDateTime,
    )
{
    enum class Status {
        AVAILABLE,
        USED,
        EXPIRED,
        CANCELLED
    }

    companion object{
        fun issue(userId: Long, couponPolicy: CouponPolicy): Coupon {
            return Coupon(
                userId = userId,
                couponPolicy = couponPolicy,
                status = Status.AVAILABLE,
                issuedAt = LocalDateTime.now(),
                expirationTime = LocalDateTime.now().plusDays(7),
            )
        }
    }

    fun use(orderId: Long, amount: Int): Int {
        require(isExpired()) { CouponExpiredException(this.id!!) }
        require(this.couponPolicy.minOrderAmount <= amount) { throw InvalidCouponException("쿠폰의 최소 사용금액: ${this.couponPolicy.minOrderAmount} 보다 높아야 합니다.") }
        require(this.status != Status.USED) { throw CouponAlreadyUsedException(this.id!!) }
        require(this.status == Status.AVAILABLE) { throw CouponNotAvailableException(this.id!!) }
        require(this.orderId == null)
        this.status = Status.USED
        this.orderId = orderId
        this.usedAt = LocalDateTime.now()

        return calculateDiscount(amount)
    }

    fun calculateDiscount(amount: Int): Int {
        return if(this.couponPolicy.discountType == CouponPolicy.DiscountType.PERCENTAGE)
        { amount * (1-this.couponPolicy.discountValue / 100.0).roundToInt()}
        else { amount-this.couponPolicy.discountValue.coerceAtLeast(0) }
    }

    fun cancel() {
        require(this.status == Status.USED) { throw InvalidCouponException("사용된 쿠폰만 취소 가능합니다.") }
        require(this.orderId != null) { throw InvalidCouponException("사용된 쿠폰의 주문ID가 유효하지 않습니다.") }
        this.orderId = null
        this.status = Status.CANCELLED
    }

    fun reIssue(): Coupon {
        require(this.status == Status.CANCELLED) { throw InvalidCouponException("취소상태가 아닌 쿠폰은 재발급 불가능합니다.") }
        require(isExpired()) { throw CouponExpiredException(this.id!!) }
        return Coupon(
            userId = this.userId,
            couponPolicy = this.couponPolicy,
            status = Status.AVAILABLE,
            issuedAt = this.issuedAt,
            expirationTime = this.expirationTime,
        )
    }

    fun isExpired(): Boolean = this.expirationTime.isAfter(LocalDateTime.now())
}

