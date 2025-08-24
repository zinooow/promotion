package jinho.han.couponservice.domain.exception

class CouponExpiredException: RuntimeException{
    constructor(couponId: Long): super("이미 만료된 쿠폰입니다. couponId: $couponId")
}