package jinho.han.couponservice.domain.exception

class InvalidCouponPeriodException: RuntimeException{
    constructor(): super()
    constructor(message: String): super(message)
}