package jinho.han.couponservice.application.exception

class ExpiredCouponException: RuntimeException{
    constructor(): super()
    constructor(message: String): super(message)
}