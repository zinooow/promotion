package jinho.han.couponservice.application.exception

class CanceledCouponException: RuntimeException{
    constructor(): super()
    constructor(message: String): super(message)
}