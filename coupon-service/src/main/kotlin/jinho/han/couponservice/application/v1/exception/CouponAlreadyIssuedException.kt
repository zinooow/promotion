package jinho.han.couponservice.application.v1.exception

class CouponAlreadyIssuedException: RuntimeException{
    constructor(couponId: Long): super("couponId:$couponId 이미 발급된 쿠폰입니다.")
}