package jinho.han.couponservice.domain.exception

class CouponAlreadyUsedException: RuntimeException{
    constructor(couponId: Long): super("couponId:$couponId 이미 사용된 쿠폰입니다.")
}