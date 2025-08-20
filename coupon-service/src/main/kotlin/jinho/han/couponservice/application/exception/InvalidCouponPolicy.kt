package jinho.han.couponservice.application.exception

class InvalidCouponPolicy: RuntimeException{
    constructor(): super()
    constructor(message: String): super(message)
}