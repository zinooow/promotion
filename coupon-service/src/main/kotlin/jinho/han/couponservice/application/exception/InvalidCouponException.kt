package jinho.han.couponservice.application.exception

class InvalidCouponException: RuntimeException{
    constructor(): super()
    constructor(message: String): super(message)
}