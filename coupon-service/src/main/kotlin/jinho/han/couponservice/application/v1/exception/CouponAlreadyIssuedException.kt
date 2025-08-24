package jinho.han.couponservice.application.v1.exception

class CouponAlreadyIssuedException: RuntimeException{
    constructor(): super("이미 해당 유저에게 발급된 쿠폰입니다.")
}