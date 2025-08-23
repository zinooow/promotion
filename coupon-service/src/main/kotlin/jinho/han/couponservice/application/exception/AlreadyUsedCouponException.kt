package jinho.han.couponservice.application.exception

class AlreadyUsedCouponException: RuntimeException{
    constructor(): super()
    constructor(message: String): super(message)
}