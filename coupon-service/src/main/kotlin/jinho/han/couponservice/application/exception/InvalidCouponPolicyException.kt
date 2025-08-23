package jinho.han.couponservice.application.exception

class InvalidCouponPolicyException: RuntimeException{
    constructor(): super()
    constructor(message: String): super(message)
}