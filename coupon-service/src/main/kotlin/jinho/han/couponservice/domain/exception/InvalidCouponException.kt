package jinho.han.couponservice.domain.exception

class InvalidCouponException: RuntimeException{
    constructor(message: String): super(message)
}