package jinho.han.couponservice.domain.exception

class CouponNotAvailableException: RuntimeException{
    constructor(couponId: Long): super("사용 가능한 쿠폰이 아닙니다. couponId: $couponId ")
}