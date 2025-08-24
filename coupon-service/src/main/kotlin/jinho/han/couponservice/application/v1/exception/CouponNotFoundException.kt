package jinho.han.couponservice.application.v1.exception

class CouponNotFoundException: RuntimeException{
    constructor(couponId: Long): super("해당 쿠폰을 찾을 수 없습니다. couponId: $couponId")
}