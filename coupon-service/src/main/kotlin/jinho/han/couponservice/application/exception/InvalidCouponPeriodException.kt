package jinho.han.couponservice.application.exception

class InvalidCouponPeriodException: RuntimeException{
    constructor(): super()
    constructor(message: String): super(message)
}