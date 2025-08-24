package jinho.han.couponservice.application.v1.exception

class CouponPolicyNotFoundException: RuntimeException{
    constructor(): super("쿠폰 정책을 찾을 수 없습니다.")
}