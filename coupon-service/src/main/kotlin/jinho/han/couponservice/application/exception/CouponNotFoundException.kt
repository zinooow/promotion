package jinho.han.couponservice.application.exception

class CouponNotFoundException: RuntimeException{
    constructor(): super()
    constructor(message: String): super(message)
}